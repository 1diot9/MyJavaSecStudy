# 反序列化黑名单调用链分析

## 概述

`com/fr/serialization/blacklist.txt` 是 FineReport 的反序列化黑名单配置文件，包含 668 个已知危险的 Java 类，用于防御反序列化漏洞攻击。

## 黑名单文件位置

- **资源路径**: `/com/fr/serialization/blacklist.txt`
- **物理位置**: JAR 包内 `fine-core-11.0.jar:com/fr/serialization/blacklist.txt`

## 黑名单加载机制

黑名单在 `JDKSerializer.CustomObjectInputStream` 类的静态初始化块中加载：

```java
// JDKSerializer.java:51-117
public static class CustomObjectInputStream extends ObjectInputStream {
    private static final Set<String> BLACK_SET = new HashSet();

    static {
        try {
            // 从资源文件加载黑名单
            InputStream resourceAsStream = CustomObjectInputStream.class
                .getResourceAsStream("/com/fr/serialization/blacklist.txt");
            BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(resourceAsStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                BLACK_SET.add(line);  // 每行一个类名
            }
        } catch (Exception e) {
            FineLoggerFactory.getLogger().info("Read black list failed: {}", e.getMessage());
        }
    }
}
```

## 黑名单检查触发点

黑名单检查在 `resolveClass` 方法中执行：

```java
// JDKSerializer.java:147-157
@Override
protected Class<?> resolveClass(ObjectStreamClass objectStreamClass)
        throws InvalidClassException, ClassNotFoundException {
    // 检查类名是否在黑名单中
    if (BLACK_SET.contains(objectStreamClass.getName())) {
        FineLoggerFactory.getLogger().error("{} hit blacklist", objectStreamClass.getName());
        throw new InvalidClassException(objectStreamClass.getName(),
            "Unsafe class blocked from deserialization");
    }
    try {
        return super.resolveClass(objectStreamClass);
    } catch (Exception e) {
        return ClassFactory.getInstance().classForName(objectStreamClass.getName());
    }
}
```

---

## WorkspaceServerInvoker#deserializeInvocation 分析

### 问题：该方法反序列化时是否会触发黑名单？

**答案：会触发黑名单检查。**

### 调用链分析

```
WorkspaceServerInvoker.deserializeInvocation(byte[], FineResult)
    │
    └─► SerializerHelper.deserialize(bArr, GZipSerializerWrapper.wrap(SafeInvocationSerializer.getDefault()))
            │
            ├─► GZipSerializerWrapper.deserialize(InputStream)
            │       │
            │       └─► SafeInvocationSerializer.deserialize(InputStream)
            │
            └─► SafeInvocationSerializer.deserialize(InputStream) [InvocationSerializer 子类]
                    │
                    ├─► generateObjectInputStream(InputStream)
                    │       │
                    │       └─► new JDKSerializer.CustomObjectInputStream(inputStream)  ✅ 黑名单检查点!
                    │
                    └─► readObject() → resolveClass() → BLACK_SET.contains()  ✅ 黑名单触发!
```

### 详细代码追踪

#### 1. 入口点 - WorkspaceServerInvoker.deserializeInvocation

```java
// WorkspaceServerInvoker.java:117-125
private Invocation deserializeInvocation(byte[] bArr, FineResult fineResult) throws Exception {
    try {
        return (Invocation) SerializerHelper.deserialize(bArr,
            GZipSerializerWrapper.wrap(SafeInvocationSerializer.getDefault()));
    } catch (Exception e) {
        fineResult.setResult(null);
        fineResult.setException(e);
        throw e;
    }
}
```

#### 2. SafeInvocationSerializer 使用黑名单版 ObjectInputStream

```java
// SafeInvocationSerializer.java:33-35
@Override
protected ObjectInputStream generateObjectInputStream(InputStream inputStream) throws IOException {
    return new JDKSerializer.CustomObjectInputStream(inputStream);  // ✅ 关键：使用带黑名单的 ObjectInputStream
}
```

#### 3. 参数反序列化也使用安全序列化器

```java
// SafeInvocationSerializer.java:28-30
@Override
protected <T> T deserialize(byte[] bArr) throws Exception {
    return (T) SerializerHelper.safeDeserialize(bArr, this.paramSerializer);
}
```

`safeDeserialize` 最终调用 `SafeSerializerSummaryAdaptor`：

```java
// SafeSerializerSummaryAdaptor.java:20-22
@Override
public T deserialize(InputStream inputStream) throws Exception {
    return (T) this.serializerSummary.safeDeserialize(inputStream);
}
```

#### 4. SerializerSummary.safeDeserialize 使用黑名单

```java
// SerializerSummary.java:131-133
public <T> T safeDeserialize(InputStream inputStream) throws Exception {
    return (T) deserialize(inputStream, true);  // true = 使用安全模式
}

// SerializerSummary.java:105-125
public <T> T deserialize(InputStream inputStream, boolean z) throws Exception {
    // z=true 时使用 CustomObjectInputStream（带黑名单）
    Serializer<T> byMark = getByMark(
        (String) new JDKSerializer.CustomObjectInputStream(inputStream).readObject(),  // ✅ 黑名单检查
        z);
    return byMark.deserialize(inputStream);
}
```

#### 5. CommonSerializer 接口定义的安全模式

```java
// CommonSerializer.java:17-19
default ObjectInputStream generateObjectInputStream(boolean z, InputStream inputStream) throws IOException {
    return z ? new JDKSerializer.CustomObjectInputStream(inputStream)  // z=true 用黑名单
             : new ObjectInputStream(inputStream);                      // z=false 不用黑名单
}
```

---

## 黑名单触发条件总结

| 场景 | 是否触发黑名单 | 说明 |
|------|---------------|------|
| `WorkspaceServerInvoker.deserializeInvocation` | ✅ 是 | 使用 `SafeInvocationSerializer`，创建 `CustomObjectInputStream` |
| `SerializerHelper.safeDeserialize` | ✅ 是 | 参数 `z=true`，使用 `CustomObjectInputStream` |
| `SerializerHelper.deserialize` (无参) | ❌ 否 | 使用普通 `ObjectInputStream`，无黑名单检查 |
| `JDKSerializer.deserialize` | ✅ 是 | 直接使用 `CustomObjectInputStream` |

---

## 安全机制分析

### 多层防护

`WorkspaceServerInvoker` 在 `handleMessage` 方法中实现了多层防护：

```java
// WorkspaceServerInvoker.java:47-51
Invocation invocation = (Invocation) SecuritySandBox.enterBlacklistRealm()
    .createDomainIfAbsent(WorkspaceConstants.CONTROLLER_CHANNEL, true, blacklistRegistry -> {
        blacklistRegistry.registerForbiddenPermission(
            new FilePermissionWrapper(FilePermissionWrapper.ALL_FILES, "write"));
    })
    .execute(() -> {
        return deserializeInvocation(bArr, fineResult);  // 在沙箱中执行反序列化
    });
```

1. **第一层**: 沙箱隔离 (`SecuritySandBox.enterBlacklistRealm()`)
2. **第二层**: 反序列化黑名单 (`CustomObjectInputStream.resolveClass()`)
3. **第三层**: 文件写入权限限制

### 异常处理

当黑名单触发时，会抛出 `InvalidClassException`，被捕获并记录安全事件：

```java
// WorkspaceServerInvoker.java:73-86
catch (SandBoxException e) {
    if (!(e.getInnerException() instanceof InvalidClassException)) {
        return serializeResult(fineResult);
    }
    InvalidClassException invalidClassException = (InvalidClassException) e.getInnerException();
    // 记录安全事件
    MetricRegistry.getMetric().submit(SecurityMessage.build(
        username, ip,
        LevelType.EMERGENCY,
        InterProviderFactory.getProvider().getLocText("Fine-Core_Serialize_Error"),
        ModuleType.REPORT,
        StringUtils.messageFormat(
            InterProviderFactory.getProvider().getLocText("Fine-Core_Serialize_Error_Msg"),
            invalidClassException.getMessage().split(";")[0])));
    return serializeResult(fineResult);
}
```

---

## 黑名单覆盖的常见利用链

黑名单包含以下常见的反序列化利用链类：

| 利用链类型 | 黑名单中的关键类 |
|-----------|-----------------|
| Commons-Collections | `InvokerTransformer`, `ChainedTransformer`, `LazyMap`, `TransformingComparator` |
| Spring | `JdkDynamicAopProxy`, `JtaTransactionManager`, `HotSwappableTargetSource` |
| JDK | `TemplatesImpl`, `EventHandler`, `BeanContextSupport` |
| Commons-BeanUtils | `BeanComparator` |
| Hibernate | `TypedValue`, `ComponentType` |
| C3P0 | `PoolBackedDataSource`, `WrapperConnectionPoolDataSource` |
| Jackson | `POJONode`, `BaseJsonNode` |

---

## 结论

`WorkspaceServerInvoker#deserializeInvocation` 方法在反序列化时**会触发黑名单检查**。触发位置有两个：

1. **InvocationPack 反序列化**: `SafeInvocationSerializer.generateObjectInputStream()` 创建 `CustomObjectInputStream`，在 `readObject()` 时通过 `resolveClass()` 检查黑名单

2. **参数反序列化**: `SafeInvocationSerializer.deserialize()` 调用 `SerializerHelper.safeDeserialize()`，最终通过 `SerializerSummary.safeDeserialize()` 使用 `CustomObjectInputStream`

这种设计确保了 RPC 调用中的反序列化操作受到黑名单保护，有效防御已知的反序列化攻击利用链。
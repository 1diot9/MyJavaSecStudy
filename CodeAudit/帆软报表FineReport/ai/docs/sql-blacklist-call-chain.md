# FineReport SQL 黑名单调用链路分析

## 概述

FineReport 中存在三处 SQL 安全黑名单配置：

1. **SQLiteInsecurityElementProvider.init()** - 数据库特定黑名单
2. **PreventSqlInjConfig** - 用户可配置的 SQL 注入防护黑名单
3. **InsecurityElementFactory** - 静态工厂方法，聚合所有黑名单

本文档以 `com.fr.function.SQL#run` 执行 SQL 语句为例，分析这三处黑名单的调用链路。

---

## 1. 黑名单来源分析

### 1.1 SQLiteInsecurityElementProvider（数据库特定黑名单）

**文件位置**: `com/fr/cbb/dialect/security/element/porvider/impl/SQLiteInsecurityElementProvider.java`

```java
public class SQLiteInsecurityElementProvider extends AbstractDbInsecurityElementProvider {
    public static final DbInsecurityElementProvider INSTANCE = new SQLiteInsecurityElementProvider();

    @Override
    protected void init() {
        register(InsecurityElementType.SQL_FUNCTION, "load_extension");
    }

    @Override
    public String acceptDbType() {
        return DatabaseNameConstants.SQLite;
    }
}
```

**特点**:
- 继承 `AbstractDbInsecurityElementProvider`，在构造函数中自动调用 `init()`
- 通过 `register()` 方法注册特定数据库的危险函数/关键字
- 以单例模式存在，在类加载时初始化

### 1.2 PreventSqlInjConfig（用户可配置黑名单）

**文件位置**: `com/fr/decision/config/PreventSqlInjConfig.java`

```java
public class PreventSqlInjConfig extends DefaultConfiguration {
    // 默认禁止关键字
    private static final String DEFAULT_FORBID_KEY =
        "\\b(?i)and\\b|\\b(?i)exec\\b|\\b(?i)insert\\b|\\b(?i)select\\b|" +
        "\\b(?i)delete\\b|\\b(?i)update\\b|\\b(?i)count\\b|\\b(?i)chr\\b|" +
        "\\b(?i)mid\\b|\\b(?i)master\\b|\\b(?i)truncate\\b|\\b(?i)char\\b|" +
        "\\b(?i)declare\\b|\\b(?i)or\\b|\\b(?i)drop\\b|\\b(?i)create\\b|" +
        "\\b(?i)alter\\b";

    // 用户选择的关键字列表
    private ColConf<Collection<String>> selectedForbidWordList;
    // 自定义关键字列表
    private ColConf<Collection<String>> customForbidWordList;
    // 特殊字符列表
    private ColConf<Collection<String>> selectedSpecialCharList;
}
```

**特点**:
- 持久化存储在配置数据库中
- 可通过管理界面动态配置
- 黑名单数据会同步到 `EscapeSqlLocalHelper`

### 1.3 InsecurityElementFactory（黑名单工厂）

**文件位置**: `com/fr/cbb/dialect/security/InsecurityElementFactory.java`

```java
public class InsecurityElementFactory {
    // 静态初始化：注册各数据库的 Provider
    static {
        PROVIDERS.put(H2InsecurityElementProvider.INSTANCE.acceptDbType(), H2InsecurityElementProvider.INSTANCE);
        PROVIDERS.put(HSQLInsecurityElementProvider.INSTANCE.acceptDbType(), HSQLInsecurityElementProvider.INSTANCE);
        PROVIDERS.put(MySQLInsecurityElementProvider.INSTANCE.INSTANCE, MySQLInsecurityElementProvider.INSTANCE);
        PROVIDERS.put(SQLServerInsecurityElementProvider.INSTANCE.acceptDbType(), SQLServerInsecurityElementProvider.INSTANCE);
        PROVIDERS.put(SQLiteInsecurityElementProvider.INSTANCE.acceptDbType(), SQLiteInsecurityElementProvider.INSTANCE);

        // 硬编码的 URL 危险参数
        FORBIDDEN_ELEMENTS_OF_URL = new InsecurityElement[]{
            new InsecurityURLParameter("INIT="),
            new InsecurityURLParameter("allowLoadLocalInfile="),
            // ... 更多危险参数
        };

        // 硬编码的校验查询危险关键字
        FORBIDDEN_ELEMENTS_OF_VALIDATION_QUERY = new InsecurityElement[]{
            new InsecuritySQLKeyword("insert"),
            new InsecuritySQLKeyword("merge"),
            // ... 更多关键字
        };
    }
}
```

**特点**:
- 聚合三类黑名单：硬编码黑名单、数据库特定黑名单、配置黑名单
- 提供统一的 `getSqlElements()`、`getUrlElements()` 等方法

---

## 2. 以 SQL#run 为例的调用链路分析

### 2.1 SQL#run 方法入口

**文件位置**: `com/fr/function/SQL.java:34`

```java
public Object run(Object[] objArr) {
    String connectionName = GeneralUtils.objectToString(objArr[0]);
    String sql = GeneralUtils.objectToString(objArr[1]);

    // 处理参数（此处调用第一个黑名单检查）
    String dsName = "SQL function:" + UUID.nameUUIDFromBytes(...);
    DataModel dataModel = SynchronizedLiveDataModelUtils.createDefaultDBDataModel(
        (DBTableData) StrategicTableData.Binder.bind(
            new DBTableData(new NameDatabaseConnection(connectionName), sql)
        ).setScope(StrategicTableData.Scope.MEDIATE).setDsName(dsName).done(),
        dealWithParameters(sql)  // 参数处理
    );
    // ...
}
```

### 2.2 第一层检查：参数值黑名单（PreventSqlInjConfig）

**调用链**:
```
SQL#run
  → dealWithParameters()                                    [SQL.java:80]
    → EscapeSqlHelper.processParametersBeforeAnalyzeSQL()   [EscapeSqlHelper.java:84]
      → EscapeSqlLocalHelper.processEscape()                [EscapeSqlLocalHelper.java:152]
        → EscapeSqlLocalHelper.hasForbidWord()              [EscapeSqlLocalHelper.java:100]
```

**关键代码**:

```java
// SQL.java:86
EscapeSqlHelper.getInstance().processParametersBeforeAnalyzeSQL(
    parameterArrProviders2Parameter,
    getCalculatorProvider()
);

// EscapeSqlLocalHelper.java:152-159
public void processEscape(ParameterProvider[] parameterProviderArr) {
    for (ParameterProvider parameterProvider : parameterProviderArr) {
        Object value = parameterProvider.getValue();
        if (value instanceof String) {
            String str = (String) value;
            if (this.useForbidWord && hasForbidWord(str)) {
                throw new RuntimeException("Fine-Core_Error_PSqlInj_Forbid_Word_Error");
            }
            // ...
        }
    }
}
```

**黑名单来源**:
- `PreventSqlInjConfig.selectedForbidWordList` → `EscapeSqlLocalHelper.forbidWordPatternSet`
- 初始化时机：`ReportActivator.start()` → `PreventSqlInjConfig.getInstance()`

**检查对象**: SQL 参数的**值**（不是 SQL 语句本身）

---

### 2.3 第二层检查：SQL 语句黑名单（InsecurityElementFactory）

**调用链**:
```
SQL#run
  → SynchronizedLiveDataModelUtils.createDefaultDBDataModel()
    → DialectExecutePreparedQueryExecutor.execute()         [DialectExecutePreparedQueryExecutor.java:16]
      → DialectExecutePreparedQueryExecutor.checkQuery()    [DialectExecutePreparedQueryExecutor.java:33]
        → JDBCSecurityChecker.checkQuery()                  [JDBCSecurityChecker.java:32]
          → InsecurityElementFactory.getSqlElements()       [InsecurityElementFactory.java:52]
```

**关键代码**:

```java
// DialectExecutePreparedQueryExecutor.java:33-35
protected void checkQuery(String sql) throws SQLException {
    JDBCSecurityChecker.checkQuery(sql);
}

// JDBCSecurityChecker.java:36-38
public static void checkQuery(String query, String dbType) throws SQLException {
    checkQuery(query, InsecurityElementFactory.getSqlElements(dbType));
}

// InsecurityElementFactory.java:64-66
public static InsecurityElement[] getSqlElements(String dbType) {
    return InsecurityElementCombiner.newInstance()
        .include(FORBIDDEN_ELEMENTS_OF_VALIDATION_QUERY)           // 硬编码关键字
        .include(getDbInsecurityElements(dbType, SQL_KEYWORD))      // 数据库特定关键字
        .include(getDbInsecurityElements(dbType, SQL_FUNCTION))     // 数据库特定函数
        .include(SQL_KEYWORD, configProvider.getBlackKeywords())    // 配置的关键字黑名单
        .include(SQL_FUNCTION, configProvider.getBlackFunctions())  // 配置的函数黑名单
        .exclude(SQL_KEYWORD, configProvider.getWhiteKeywords())    // 白名单排除
        .exclude(SQL_FUNCTION, configProvider.getWhiteFunctions())  // 白名单排除
        .get();
}
```

**黑名单来源**:
1. `FORBIDDEN_ELEMENTS_OF_VALIDATION_QUERY` - 硬编码（insert, merge, attach, xp_dirtree, VACUUM）
2. `SQLiteInsecurityElementProvider` 等数据库特定黑名单 - 通过 `getDbInsecurityElements()`
3. `JDBCSecurityConfiguration` - 通过 `ConfigInsecurityElementProviderImpl`

**初始化时机**:
- `ConfigurationActivator.start()` → `InsecurityElementFactory.setConfigInsecurityElementProvider()`
- `ReportActivator.start()` → `DatasourceApiFactory.setJdbcSecurityConfigurationProvider()`

**检查对象**: SQL 语句本身（不是参数值）

---

## 3. SQLiteInsecurityElementProvider 的 init() 调用时机

**初始化链**:
```
JVM 类加载
  → InsecurityElementFactory.<clinit>()                     [静态初始化块]
    → SQLiteInsecurityElementProvider.INSTANCE              [静态字段访问]
      → new SQLiteInsecurityElementProvider()               [构造函数]
        → AbstractDbInsecurityElementProvider()             [父类构造函数]
          → init()                                          [调用子类实现]
            → register(SQL_FUNCTION, "load_extension")      [注册危险函数]
```

**关键代码**:

```java
// AbstractDbInsecurityElementProvider.java:16-18
public AbstractDbInsecurityElementProvider() {
    init();  // 构造时自动调用
}
```

**时序**:
1. 当 `InsecurityElementFactory` 类被加载时，静态初始化块执行
2. 静态块中创建 `SQLiteInsecurityElementProvider.INSTANCE`
3. 创建实例时自动调用 `init()` 方法
4. `init()` 方法通过 `register()` 将数据库特定的危险元素添加到内部 Map

---

## 4. 三处黑名单的调用关系总结

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         SQL#run 执行流程                                  │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│  1. 参数处理阶段                                                         │
│     ┌──────────────────────────────────────────────────────────────┐   │
│     │ dealWithParameters()                                          │   │
│     │   ↓                                                           │   │
│     │ EscapeSqlHelper.processParametersBeforeAnalyzeSQL()          │   │
│     │   ↓                                                           │   │
│     │ EscapeSqlLocalHelper.hasForbidWord()                          │   │
│     │   ↓                                                           │   │
│     │ ★ PreventSqlInjConfig 黑名单 ←────────────────────────────┐  │   │
│     │   (检查 SQL 参数值是否包含禁止关键字)                         │  │   │
│     └──────────────────────────────────────────────────────────────┘   │
│                                                                         │
│  2. SQL 执行阶段                                                         │
│     ┌──────────────────────────────────────────────────────────────┐   │
│     │ DialectExecutePreparedQueryExecutor.execute()                │   │
│     │   ↓                                                           │   │
│     │ JDBCSecurityChecker.checkQuery()                              │   │
│     │   ↓                                                           │   │
│     │ InsecurityElementFactory.getSqlElements()                     │   │
│     │   ↓                                                           │   │
│     │ ┌─────────────────────────────────────────────────────────┐  │   │
│     │ │ ★ InsecurityElementFactory 静态黑名单                    │  │   │
│     │ │   - FORBIDDEN_ELEMENTS_OF_VALIDATION_QUERY (硬编码)     │  │   │
│     │ │                                                         │  │   │
│     │ │ ★ SQLiteInsecurityElementProvider.init() 注册的黑名单   │  │   │
│     │ │   - 通过 getDbInsecurityElements(dbType, type) 获取     │  │   │
│     │ │                                                         │  │   │
│     │ │ ★ JDBCSecurityConfiguration 配置黑名单                  │  │   │
│     │ │   - 通过 ConfigInsecurityElementProviderImpl 获取       │  │   │
│     └──────────────────────────────────────────────────────────────┘   │
│     │   (检查 SQL 语句是否包含危险元素)                               │   │
│     └──────────────────────────────────────────────────────────────┘   │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 5. 调用顺序总结

| 顺序 | 检查点 | 黑名单来源 | 检查对象 | 调用位置 |
|------|--------|-----------|---------|---------|
| 1 | 参数值检查 | `PreventSqlInjConfig` | SQL 参数值 | `EscapeSqlLocalHelper.hasForbidWord()` |
| 2 | SQL 语句检查 | `InsecurityElementFactory` (聚合) | SQL 语句 | `JDBCSecurityChecker.checkQuery()` |

### 黑名单聚合顺序（InsecurityElementFactory.getSqlElements()）

1. **硬编码黑名单**: `FORBIDDEN_ELEMENTS_OF_VALIDATION_QUERY`
2. **数据库特定黑名单**: 通过 `SQLiteInsecurityElementProvider` 等获取
3. **配置黑名单**: 通过 `ConfigInsecurityElementProviderImpl` 获取
4. **应用白名单**: 排除白名单中的关键字

---

## 6. 关键发现

1. **两套独立的黑名单系统**:
   - `PreventSqlInjConfig` 用于检查参数值，数据可动态配置
   - `InsecurityElementFactory` 用于检查 SQL 语句，聚合多种来源

2. **SQLiteInsecurityElementProvider.init() 不直接被 SQL#run 调用**:
   - 它在 `InsecurityElementFactory` 类加载时被初始化
   - 通过 `getSqlElements()` 方法间接参与 SQL 检查

3. **黑名单检查的时机不同**:
   - 参数检查：在参数解析阶段，使用正则匹配参数值
   - SQL 检查：在执行前，检查整个 SQL 语句

4. **JDBCSecurityChecker 还用于其他场景**:
   - JDBC URL 检查：`checkUrl()`
   - 连接验证查询检查：`checkValidationQuery()`
   - 这些检查在数据源配置时触发，不在 SQL#run 中调用
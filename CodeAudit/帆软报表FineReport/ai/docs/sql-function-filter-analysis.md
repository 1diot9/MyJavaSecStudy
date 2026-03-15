# FineReport SQL 函数过滤机制分析

## 概述

本文档分析 `com.fr.function.SQL#run` 方法中 SQL 语句的过滤机制，追踪完整的过滤链路和安全控制点。

## 调用链路

```
SQL#run()
  │
  ├─ 参数解析
  │     strObjectToString  -> 数据连接名
  │     strObjectToString2 -> SQL 语句 (未过滤!)
  │
  └─ dealWithParameters(sql)
        │
        ├─ SqlUtils.clearSqlComments(sql)
        │     清除 SQL 注释: -- 和 /* */
        │
        ├─ ParameterHelper.analyze4Parameters()
        │     提取 ${param} 形式的参数
        │
        └─ EscapeSqlHelper.processParametersBeforeAnalyzeSQL()
              │
              └─ EscapeSqlLocalHelper.processEscape()
                    对参数值进行过滤 (非 SQL 语句本身)
```

## 核心源码分析

### 1. SQL 函数入口 (`SQL.java:34-77`)

```java
@RestrictScript
public class SQL extends AbstractFunction {
    @Override
    public Object run(Object[] objArr) {
        String strObjectToString = GeneralUtils.objectToString(objArr[0]);  // 数据连接名
        String strObjectToString2 = GeneralUtils.objectToString(objArr[1]); // SQL 语句 - 未过滤!

        // SQL 语句直接传入 DBTableData，未经过滤
        DataModel dataModel = SynchronizedLiveDataModelUtils.createDefaultDBDataModel(
            (DBTableData) StrategicTableData.Binder.bind(
                new DBTableData(new NameDatabaseConnection(strObjectToString), strObjectToString2)
            )
        );
        // ...
    }
}
```

**关键发现**: SQL 语句 `strObjectToString2` 直接传入执行，未经过滤。

### 2. 参数处理 (`SQL.java:79-88`)

```java
private String dealWithParameters(String str) {
    // 1. 清除 SQL 注释
    Parameter[] params = ParameterHelper.analyze4Parameters(
        new String[]{SqlUtils.clearSqlComments(str)}, false
    );

    if (params.length == 0) {
        return str;
    }

    // 2. 解析参数值
    Parameter[] resolvedParams = Parameter.providers2Parameter(
        Calculator.processParameters(getCalculatorProvider(), params)
    );

    // 3. 核心: 过滤参数值 (非 SQL 语句)
    EscapeSqlHelper.getInstance().processParametersBeforeAnalyzeSQL(
        resolvedParams, getCalculatorProvider()
    );

    return ParameterHelper.analyzeCurrentContextTableData4Templatee(str, resolvedParams);
}
```

### 3. SQL 注释清除 (`SqlUtils.java:82-145`)

```java
public static String clearSqlComments(String str) {
    // 清除规则:
    // - 单行注释: -- 开头到行尾
    // - 多行注释: /* ... */
    // 保留字符串内的内容 (单引号/双引号内不处理)

    char[] charArray = str.toCharArray();
    boolean singleLineComment = false;
    boolean multiLineComment = false;
    boolean inSingleQuotes = false;
    boolean inDoubleQuotes = false;
    // ... 状态机处理
}
```

### 4. 核心过滤逻辑 (`EscapeSqlLocalHelper.java:152-199`)

```java
public void processEscape(ParameterProvider[] parameterProviderArr) {
    for (ParameterProvider param : parameterProviderArr) {
        Object value = param.getValue();

        if (value instanceof String) {
            String str = (String) value;

            // 检测禁用词
            if (this.useForbidWord && hasForbidWord(str)) {
                throw new RuntimeException("Fine-Core_Error_PSqlInj_Forbid_Word_Error");
            }

            // 转义特殊字符
            if (this.useEscapeSpecialChar) {
                param.setValue(escapeSpecialChar(str));
            }
        }
        // ... 数组和集合类型的类似处理
    }
}
```

### 5. 禁用词检测 (`EscapeSqlLocalHelper.java:100-111`)

```java
public boolean hasForbidWord(String str) {
    if (!this.useForbidWord) {
        return false;  // 功能关闭则直接放行
    }

    // 使用正则匹配
    for (Pattern pattern : this.forbidWordPatternSet) {
        if (pattern.matcher(str).find()) {
            return true;
        }
    }
    return false;
}
```

## 过滤规则配置

### 配置类 (`PreventSqlInjConfig.java`)

```java
public class PreventSqlInjConfig extends DefaultConfiguration {
    // 默认禁用词 (正则表达式，| 分隔)
    private static final String DEFAULT_FORBID_KEY =
        "\\b(?i)and\\b|\\b(?i)exec\\b|\\b(?i)insert\\b|\\b(?i)select\\b|" +
        "\\b(?i)delete\\b|\\b(?i)update\\b|\\b(?i)count\\b|\\b(?i)chr\\b|" +
        "\\b(?i)mid\\b|\\b(?i)master\\b|\\b(?i)truncate\\b|\\b(?i)char\\b|" +
        "\\b(?i)declare\\b|\\b(?i)or\\b|\\b(?i)drop\\b|\\b(?i)create\\b|\\b(?i)alter\\b";

    // 默认特殊字符
    private static final String DEFAULT_SPECIAL_CHAR = "'|;|--";

    // 配置开关
    private Conf<Boolean> useForbidWord = Holders.simple(true);        // 默认启用
    private Conf<Boolean> useEscapeSpecialChar = Holders.simple(false); // 默认不转义
}
```

### 默认过滤规则详情

| 类型 | 规则 | 说明 |
|------|------|------|
| 禁用词 | `and, exec, insert, select, delete, update, count, chr, mid, master, truncate, char, declare, or, drop, create, alter` | 不区分大小写，单词边界匹配 |
| 特殊字符 | `'`, `;`, `--` | 单引号、分号、注释符 |

### 另一套过滤规则 (`SqlParamCheckerToolImpl.java`)

```java
// 注意: 这是另一套独立的过滤规则
private static final String DEFAULT_FORBID_KEY =
    "\\b(?i)and\\b|\\b(?i)exec\\b|\\b(?i)insert\\b|\\b(?i)select\\b|" +
    "\\b(?i)delete\\b|\\b(?i)update\\b|\\b(?i)count\\b|\\b(?i)chr\\b|" +
    "\\b(?i)mid\\b|\\b(?i)master\\b|\\b(?i)truncate\\b|\\b(?i)char\\b|" +
    "\\b(?i)declare\\b|\\b(?i)or\\b|\\b(?i)drop\\b|\\b(?i)create\\b|\\b(?i)alert\\b";
    // 注意: 这里是 "alert" 而非 "alter" (可能是笔误)

private static final String DEFAULT_SPECIAL_CHAR = "'|\"|;|--|#|=|\\(|\\)|%|_|\\\\";  // 更严格
```

## 安全风险分析

### 1. SQL 语句本身不过滤

**问题**: `SQL#run` 的第二个参数直接作为 SQL 语句执行，未经过滤。

```java
String sql = GeneralUtils.objectToString(objArr[1]);  // 用户可控
// 直接传入执行，无过滤
new DBTableData(new NameDatabaseConnection(connName), sql)
```

**影响**: 攻击者可在 SQL 函数调用时注入任意 SQL。

### 2. 默认配置不一致

| 位置 | useForbidWord | useEscapeSpecialChar |
|------|---------------|---------------------|
| `EscapeSqlLocalHelper` 构造函数 | `false` | `false` |
| `PreventSqlInjConfig` 默认值 | `true` | `false` |

**问题**: `EscapeSqlLocalHelper` 实例化时默认关闭过滤，需等待配置加载后才启用。

### 3. 正则匹配可绕过

**问题**: 使用简单的正则匹配，可通过以下方式绕过:
- Unicode 编码: `\u0073\u0065\u006c\u0065\u0063\u0074` (select)
- 大小写混淆: 已处理 (使用 `(?i)` 忽略大小写)
- 注释混淆: `sel/**/ect` (注释已在参数处理前清除，但字符串内注释不清除)
- 编码绕过: URL 编码、HTML 实体等

### 4. 特殊字符转义默认关闭

**问题**: `useEscapeSpecialChar = false`，特殊字符不会被移除。

```java
if (this.useEscapeSpecialChar) {
    param.setValue(escapeSpecialChar(str));  // 默认不执行
}
```

## 完整调用关系图

```
┌─────────────────────────────────────────────────────────────────┐
│                        SQL Function 入口                         │
│                     com.fr.function.SQL#run()                   │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                      参数处理入口                                │
│               dealWithParameters(sqlStatement)                  │
└─────────────────────────────────────────────────────────────────┘
                                │
            ┌───────────────────┼───────────────────┐
            ▼                   ▼                   ▼
┌───────────────────┐ ┌─────────────────┐ ┌─────────────────────────┐
│ SqlUtils          │ │ ParameterHelper │ │ EscapeSqlHelper         │
│ clearSqlComments  │ │ analyze4Params  │ │ processParameters       │
│ (清除注释)         │ │ (提取参数)       │ │ (过滤参数值)            │
└───────────────────┘ └─────────────────┘ └─────────────────────────┘
                                                    │
                                                    ▼
                              ┌─────────────────────────────────────┐
                              │ EscapeSqlLocalHelper.processEscape  │
                              │                                     │
                              │  useForbidWord ──→ hasForbidWord()  │
                              │  useEscapeSpecialChar               │
                              │       └──→ escapeSpecialChar()      │
                              └─────────────────────────────────────┘
                                                    │
                                                    ▼
                              ┌─────────────────────────────────────┐
                              │ PreventSqlInjConfig                 │
                              │ (配置来源)                           │
                              │                                     │
                              │ DEFAULT_FORBID_KEY                  │
                              │ DEFAULT_SPECIAL_CHAR                │
                              │ useForbidWord = true                │
                              │ useEscapeSpecialChar = false        │
                              └─────────────────────────────────────┘
```

## 相关文件清单

| 文件 | 路径 | 功能 |
|------|------|------|
| SQL.java | `com/fr/function/SQL.java` | SQL 函数入口 |
| SqlUtils.java | `com/fr/general/sql/SqlUtils.java` | SQL 注释清除 |
| EscapeSqlHelper.java | `com/fr/data/impl/EscapeSqlHelper.java` | 过滤器门面 |
| EscapeSqlLocalHelper.java | `com/fr/data/impl/escapesql/local/EscapeSqlLocalHelper.java` | 核心过滤逻辑 |
| PreventSqlInjConfig.java | `com/fr/decision/config/PreventSqlInjConfig.java` | 配置管理 |
| SqlParamCheckerToolImpl.java | `com/fr/security/api/impl/SqlParamCheckerToolImpl.java` | 另一套过滤实现 |

## 结论

FineReport 的 SQL 函数过滤机制存在以下问题:

1. **SQL 语句本身不过滤** - 最严重的问题，可直接注入
2. **仅过滤参数值** - `${param}` 形式的参数值被检测
3. **正则匹配可绕过** - 编码、注释等方式
4. **特殊字符转义默认关闭** - 配置不严格

建议在代码审计时重点关注直接调用 `SQL#run` 或类似函数的接口点。
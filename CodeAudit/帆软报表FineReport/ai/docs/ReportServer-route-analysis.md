# ReportServer 路由处理流程分析

## 概述

`/webroot/ReportServer` 是 FineReport 报表引擎的核心 Servlet 路由，专门处理报表预览、渲染和导出等请求。与 `/webroot/decision`（决策平台）不同，ReportServer 是独立的报表引擎 Servlet。

---

## 路由配置

### Servlet 注册

**注册位置**: `com/fr/report/ReportActivator.java:178-191`

```java
// 第184行：动态注册 ReportServlet
ServletRegistration.Dynamic dynamicAddServlet = servletContext.addServlet(
    ServerConfig.getInstance().getReportServletName(),  // "ReportServer"
    new ReportServlet()
);
// 第186行：映射到 /ReportServer/*
dynamicAddServlet.addMapping(new String[]{ServerConfig.getInstance().getReportServletMapping()});
```

### 默认配置

**配置类**: `com/fr/base/ServerConfig.java`

```java
// 第20行
private Conf<String> reportServletName = Holders.simple("ReportServer");

// 第74-76行
public String getReportServletMapping() {
    return "/" + this.reportServletName.get() + "/*";
}
```

---

## 请求处理链

### 整体架构

```
/webroot/ReportServer/*
         │
         ▼
┌─────────────────────────────────┐
│  com.fr.web.ReportServlet       │  处理集群重定向检查
│  (继承自 BaseServlet)            │
└─────────────────────────────────┘
         │ service()
         ▼
┌─────────────────────────────────┐
│  com.fr.web.BaseServlet         │  初始化响应、安全头
│  doGet() → doPost()              │
└─────────────────────────────────┘
         │ 第66行
         ▼
┌─────────────────────────────────┐
│  com.fr.web.core.ReportDispatcher │  核心分发器
│  dealWithRequest()               │
└─────────────────────────────────┘
         │
         ├─ op参数? → dealWithOp() → Service分发
         │
         └─ 无op参数? → WebletFactory.createWebletByRequest()
                              │
                              ▼
                    ┌─────────────────────┐
                    │ WebletCreator实现类  │
                    │ - ReportletCreator  │  处理 reportlet/viewlet 参数
                    │ - FormletCreator    │  处理 form 参数
                    │ - GroupReportletCreator │
                    └─────────────────────┘
```

### 1. ReportServlet（入口 Servlet）

**文件**: `com/fr/web/ReportServlet.java`

```java
public void service(ServletRequest servletRequest, ServletResponse servletResponse)
    throws ServletException, IOException {
    if (servletRequest instanceof HttpServletRequest) {
        try {
            // 失败请求处理
            if (!FailedRequestHandler.handle((HttpServletRequest) servletRequest,
                (HttpServletResponse) servletResponse)) {
                return;
            }
            // 集群重定向检查
            if (!this.reportClusterRedirectInterceptor.preHandle(
                (HttpServletRequest) servletRequest,
                (HttpServletResponse) servletResponse, null)) {
                return;
            }
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }
    // 调用父类 BaseServlet 处理
    super.service(servletRequest, servletResponse);
}
```

### 2. BaseServlet（基础处理）

**文件**: `com/fr/web/BaseServlet.java`

主要职责：
- 初始化 Gzip 响应包装器
- 添加安全响应头（P3P）
- 调用 `ReportDispatcher.dealWithRequest()` 处理请求
- 异常处理和错误响应

```java
public void doGet(HttpServletRequest httpServletRequest,
    HttpServletResponse httpServletResponse) throws ServletException, IOException {
    saveRequestContext(httpServletRequest);
    // ... 初始化响应 ...

    // 核心调用：第66行
    ReportDispatcher.dealWithRequest(httpServletRequest, httpServletResponse);

    // ... 清理和异常处理 ...
}
```

### 3. ReportDispatcher（核心分发器）

**文件**: `com/fr/web/core/ReportDispatcher.java`

核心方法 `dealWithRequest()` 处理逻辑：

```java
public static void dealWithRequest(HttpServletRequest httpServletRequest,
    HttpServletResponse httpServletResponse) throws Exception {

    String op = WebUtils.getHTTPRequestParameter(httpServletRequest, "op");
    String sessionID = WebUtils.getHTTPRequestParameter(httpServletRequest, "sessionID");

    // 1. 特殊操作处理
    if ("closesessionid".equalsIgnoreCase(op) && sessionID != null) {
        closeSession(httpServletRequest, httpServletResponse, sessionID);
        return;
    }

    // 2. 会话管理
    if (sessionID != null) {
        SessionPoolManager.updateSessionID(sessionID);
        // ...
    }

    // 3. 获取会话ID操作
    if ("getSessionID".equalsIgnoreCase(op)) {
        sessionID = SessionPoolManager.generateSessionIDWithCheckRegister(
            httpServletRequest, httpServletResponse);
        SessionPoolManager.responseSessionID(sessionID, httpServletRequest, httpServletResponse);
        return;
    }

    // 4. 分发请求
    dealWeblet(op, sessionID, httpServletRequest, httpServletResponse);
}
```

---

## URL 参数处理

### 常见请求参数

| 参数 | 说明 | 处理方式 |
|------|------|----------|
| `op` | 操作类型 | `ReportDispatcher.dealWithOp()` 分发到对应 Service |
| `reportlet` | 报表模板路径 | `ReportletCreator` 创建 `TemplateReportlet` |
| `viewlet` | 报表模板路径（兼容） | `ReportletCreator` 创建 `TemplateReportlet` |
| `form` | 表单模板路径 | `FormletCreator` 创建 FormWeblet |
| `sessionID` | 会话标识 | `SessionPoolManager` 管理会话 |

### op 参数处理

**文件**: `com/fr/web/core/ReportDispatcher.java:251-281`

```java
private static void dealWithOp(String op, String sessionID,
    HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
    throws Exception {

    String opLower = op.toLowerCase();
    httpServletRequest.setAttribute("op", opLower);

    // 1. 查找扩展服务
    Service service = extraServices.get(opLower);
    if (service != null) {
        service.process(httpServletRequest, httpServletResponse, opLower, sessionID);
        return;
    }

    // 2. 查找默认服务
    Service service2 = servicesAvailable.get(opLower);
    if (service2 != null) {
        service2.process(httpServletRequest, httpServletResponse, opLower, sessionID);
        return;
    }

    // 3. 动态服务
    Class<? extends Service> cls = dynamicServiceMap.get(opLower);
    if (cls != null) {
        Service newService = cls.newInstance();
        newService.process(httpServletRequest, httpServletResponse, opLower, sessionID);
        registerGroupService(new Service[]{newService});
        return;
    }

    // 4. 未识别的操作
    PrintWriter writer = WebUtils.createPrintWriter(httpServletResponse);
    writer.println("Unresolvable Operation:" + StableUtils.replaceScript4Xss(opLower));
}
```

### 默认注册的 Service

**文件**: `com/fr/web/core/ReportDispatcher.java:75-77`

```java
private static Service[] initServiceArray() {
    return new Service[]{
        ResourceService.getInstance(),    // 资源服务
        AttachmentService.getInstance(),  // 附件服务
        RegProcessorService.getInstance(), // 注册处理
        ErrorService.getInstance()        // 错误服务
    };
}
```

---

## reportlet 参数处理流程

### 1. WebletFactory 工厂类

**文件**: `com/fr/web/factory/WebletFactory.java`

```java
public static Weblet createWebletByRequest(HttpServletRequest httpServletRequest,
    HttpServletResponse httpServletResponse) throws Exception {

    for (WebletCreator webletCreator : webletCreatorList) {
        if (webletCreator.match(webletCreator.queryPath(httpServletRequest).getPath())) {
            Weblet weblet = webletCreator.createWebletByRequest(
                httpServletRequest, httpServletResponse);
            if (weblet != null) {
                return weblet;
            }
        }
    }
    return null;
}
```

### 2. ReportletCreator（报表预览创建器）

**文件**: `com/fr/web/reportlet/ReportletCreator.java`

```java
public class ReportletCreator extends AbstractWebletCreator {

    // 支持的路径标记
    @Override
    public TemplatePathMarker[] queryPathMarker() {
        return new TemplatePathMarker[]{
            PathMarkerImpl.VIEWLET,   // viewlet 参数
            PathMarkerImpl.REPORTLET  // reportlet 参数
        };
    }

    // 支持的文件扩展名
    @Override
    public FileExtension suffix() {
        return FileExtension.CPT;  // .cpt 报表文件
    }

    // 创建 Weblet
    @Override
    public Weblet createWebletByRequest(HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse) {

        TemplatePathNode pathNode = queryPath(httpServletRequest);
        String path = pathNode.getPath();

        // 旧版 Weblet 检查
        if (oldWebletOrServletCheck(httpServletRequest, pathNode)) {
            return OldWeblet.asOldReportlet(path);
        }

        // 扩展名检查
        if (ComparatorUtils.equals(
            NetworkHelper.getHTTPRequestParameter(httpServletRequest, "ext"), "x")) {
            return new TemplateReportlet(path + "x");
        }

        return new TemplateReportlet(path);
    }
}
```

### 3. TemplateReportlet 处理

当 `reportlet` 或 `viewlet` 参数存在时，`ReportletCreator` 会创建 `TemplateReportlet` 对象来处理报表模板。

**典型请求示例**:
```
/webroot/ReportServer?reportlet=xxx.cpt
/webroot/ReportServer?viewlet=xxx.cpt
```

---

## FormletCreator（表单处理）

**文件**: `com/fr/web/weblet/FormletCreator.java`

处理 `form` 参数，用于表单模板预览：

```java
public TemplatePathMarker[] queryPathMarker() {
    return new TemplatePathMarker[]{PathMarkerImpl.FORM};
}

@Override
public FileExtension suffix() {
    return FileExtension.FRM;  // .frm 表单文件
}
```

**典型请求示例**:
```
/webroot/ReportServer?form=xxx.frm
```

---

## Spring MVC 集成

### ReportConfiguration

**文件**: `com/fr/report/ReportConfiguration.java`

```java
@ComponentScan(basePackages = {"com.fr.web.controller", "com.fr.web.service"})
@EnableWebMvc
@Configuration
public class ReportConfiguration extends FineWebApplicationConfiguration {
}
```

扫描的包：
- `com.fr.web.controller` - 控制器
- `com.fr.web.service` - 服务类

---

## 与 decision 路由的对比

| 路由 | Servlet | 配置位置 | 用途 |
|------|---------|----------|------|
| `/webroot/decision/*` | `FineDispatcherServlet` | `DecisionServletInitializer` | 决策平台管理接口 |
| `/webroot/ReportServer/*` | `ReportServlet` | `ReportActivator.initReportServlet()` | 报表预览、渲染、导出 |

### 架构差异

| 特性 | decision | ReportServer |
|------|----------|--------------|
| 基础框架 | Spring MVC | Servlet + Weblet 模式 |
| 主要功能 | 用户管理、权限、配置 | 报表预览、导出 |
| 拦截器链 | 多层拦截器 | 简单拦截器 |
| 会话管理 | FineSession | SessionProvider |

---

## 关键源码位置索引

| 组件 | 文件路径 |
|------|----------|
| Servlet 注册 | `com/fr/report/ReportActivator.java:178-191` |
| Servlet 入口 | `com/fr/web/ReportServlet.java` |
| 基础 Servlet | `com/fr/web/BaseServlet.java` |
| 请求分发器 | `com/fr/web/core/ReportDispatcher.java` |
| Weblet 工厂 | `com/fr/web/factory/WebletFactory.java` |
| 报表创建器 | `com/fr/web/reportlet/ReportletCreator.java` |
| 表单创建器 | `com/fr/web/weblet/FormletCreator.java` |
| Spring 配置 | `com/fr/report/ReportConfiguration.java` |
| 配置管理 | `com/fr/base/ServerConfig.java` |

---

## 安全审计关注点

1. **参数注入**: `reportlet`/`viewlet` 参数可能存在路径遍历风险
2. **Service 动态加载**: `dynamicServiceMap` 允许动态注册服务，需关注注入风险
3. **会话管理**: `SessionPoolManager` 的会话创建和销毁逻辑
4. **异常处理**: 错误信息可能泄露敏感路径信息
# 帆软模块启动机制与 Servlet 注册流程

## 一、概述

帆软（FineReport）采用模块化架构设计，通过 XML 配置文件定义模块层次结构，使用 `Activator` 模式管理模块生命周期。本文档详细分析帆软的启动流程和 Servlet 动态注册机制。

## 二、模块配置文件结构

### 2.1 配置文件层次结构

```
resources/com/fr/config/
├── starter/
│   ├── server-startup.xml        # 服务端启动入口配置
│   └── designer-startup.xml      # 设计器启动入口配置
├── server/
│   ├── server.xml                # 服务端核心模块配置
│   ├── server-start-basic.xml    # 服务端基础模块配置
│   ├── server-core.xml           # 服务端核心配置
│   ├── decision.xml              # 决策平台模块配置
│   └── server-workspace.xml      # 工作空间配置
├── base/
│   ├── core.xml                  # 核心模块配置
│   ├── basic.xml                 # 基础模块配置
│   └── log.xml                   # 日志模块配置
└── function/
    └── function-base.xml         # 函数基础模块配置
```

### 2.2 主启动配置文件

**文件位置**: `resources/com/fr/config/starter/server-startup.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<server-startup role="root">
    <server-basic ref="../server/server-start-basic.xml"/>
    <server ref="../server/server.xml"/>
    <server-deployment-only ref="../server/server-deployment-only.xml"/>
</server-startup>
```

**配置说明**:
- `role="root"`: 标记为根模块
- `ref` 属性: 引用其他配置文件，实现配置复用

### 2.3 服务端核心模块配置

**文件位置**: `resources/com/fr/config/server/server.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!--服务器功能部分-->
<server activator="com.fr.stable.web.ServerActivator" role="server-root" binding-workspace="local">
    <server-core ref="../server/server-core.xml"/>

    <!--新平台（决策系统）-->
    <decision ref="decision.xml"/>

    <!--websocket模块启动-->
    <websocket activator="com.fr.web.socketio.WebSocketActivator"/>

    <!--报表计算引擎相关部分-->
    <report activator="com.fr.report.ReportActivator" version="11.0" level="10">
        <report-esd activator="com.fr.esd.ESDActivator"/>
        <report-write activator="com.fr.report.write.WriteActivator"/>
        <report-analysis activator="com.fr.analysis.activator.ReportAnalysisActivator"/>
        <report-nx activator="com.fr.nx.app.NXActivator"/>
        <report-vcs activator="com.fr.workspace.server.vcs.VcsFolderManagerActivator"/>
    </report>

    <!--平台针对报表的扩展部分-->
    <decision-report activator="com.fr.decision.extension.report.DecisionActivator">
        <decision-workflow activator="com.fr.decision.workflow.util.WorkflowActivator"/>
    </decision-report>

    <!-- ... 其他模块 ... -->
</server>
```

**配置属性说明**:

| 属性 | 说明 | 示例 |
|------|------|------|
| `activator` | 模块激活器类名 | `com.fr.report.ReportActivator` |
| `version` | 模块版本 | `11.0` |
| `level` | 模块层级（影响启动顺序） | `10` |
| `role` | 模块角色标识 | `server-root` |
| `binding-workspace` | 工作空间绑定 | `local` |

### 2.4 决策平台模块配置

**文件位置**: `resources/com/fr/config/server/decision.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!--平台部分-->
<decision activator="com.fr.decision.DecisionActivator" version="11.0" level="10">

    <!--初始化Servlet-->
    <servlet-init activator="com.fr.decision.base.DecisionServletInitializer"/>

    <!--新平台db-->
    <db activator="com.fr.decision.db.DecisionDBActivator"/>

    <service-context activator="com.fr.decision.service.ServiceContextActivator"/>
    <service activator="com.fr.decision.service.ServiceActivator"/>
    <authority activator="com.fr.decision.authority.AuthorityActivator"/>

    <webservice activator="com.fr.decision.webservice.WebServiceActivator">
        <datasource-connection activator="com.fr.decision.webservice.datasource.DatasourceConnectionActivator"/>
        <url-alias activator="com.fr.decision.webservice.url.URLAliasActivator"/>
        <register-service activator="com.fr.decision.webservice.RegisterServiceActivator"/>
        <!-- ... 其他子模块 ... -->
    </webservice>

    <!-- ... 其他模块 ... -->
</decision>
```

## 三、启动流程详解

### 3.1 完整启动链路

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           帆软服务端启动流程                                   │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  1. Tomcat 容器启动                                                          │
│       │                                                                     │
│       │ 扫描 ServletContainerInitializer                                    │
│       ▼                                                                     │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │ SpringServletContainerInitializer                                    │   │
│  │ (Spring 提供的启动入口)                                               │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│       │                                                                     │
│       │ 发现 WebApplicationInitializer 实现                                │
│       ▼                                                                     │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │ FineWebApplicationInitializer.onStartup()                           │   │
│  │ 源码: com/fr/startup/FineWebApplicationInitializer.java:15          │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│       │                                                                     │
│       │ FineWebApplicationStartup.getInstance().start()                   │
│       ▼                                                                     │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │ FineWebApplicationStartup.executeStart()                            │   │
│  │ 源码: com/fr/startup/FineWebApplicationStartup.java:57-74           │   │
│  │                                                                     │   │
│  │   // 1. 创建 Spring 上下文                                          │   │
│  │   springContext = new AnnotationConfigWebApplicationContext();      │   │
│  │                                                                     │   │
│  │   // 2. 解析模块配置文件                                             │   │
│  │   serverRoot = getServerModule();                                   │   │
│  │                                                                     │   │
│  │   // 3. 注册 ServletContext 到模块上下文                             │   │
│  │   serverRoot.setSingleton(ServletContext.class, context);           │   │
│  │                                                                     │   │
│  │   // 4. 启动所有模块                                                 │   │
│  │   serverRoot.start();                                               │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│       │                                                                     │
│       │ ModuleContext.parseRoot("server-startup.xml")                      │
│       ▼                                                                     │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │ FineModuleParser.parse() → ModuleBuilder.build()                    │   │
│  │                                                                     │   │
│  │ 解析 XML 配置文件:                                                   │   │
│  │   server-startup.xml                                                │   │
│  │     ├── server-start-basic.xml                                      │   │
│  │     │     ├── basic.xml                                             │   │
│  │     │     ├── core.xml                                              │   │
│  │     │     └── function-base.xml                                     │   │
│  │     └── server.xml                                                  │   │
│  │           ├── decision.xml                                          │   │
│  │           │     └── DecisionServletInitializer                      │   │
│  │           └── ReportActivator                                       │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│       │                                                                     │
│       ▼                                                                     │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │ FineModule.start() → ParentFirstStrategy.doStart()（默认策略）       │   │
│  │                                                                     │   │
│  │ 按顺序启动模块（先父模块，后子模块）:                                  │   │
│  │   1. ServerActivator.start()                                        │   │
│  │   2. DecisionActivator.start()                                      │   │
│  │      └── DecisionServletInitializer.start() ← 注册 decision Servlet │   │
│  │   3. ReportActivator.start() ← 注册 ReportServer Servlet            │   │
│  │   4. ... 其他模块 ...                                                │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 3.2 模块解析与实例化（递归构建）

**源码位置**: `com/fr/module/engine/build/ModuleBuilder.java:40-48, 80-97, 104-114`

`ModuleBuilder#build()` 方法会被**递归调用多次**，每个 XML 节点都会触发一次调用，最终创建出完整的模块树结构。

```java
// 入口方法
static FineModule build(ModuleConfig moduleConfig) {
    FineModule fineModuleBuild = build(moduleConfig, null);  // 第一次调用
    finishLink(fineModuleBuild);
    ActivatorExtension.executePrepare(fineModuleBuild);
    return fineModuleBuild;
}

// 递归构建方法
private static FineModule build(ModuleConfig moduleConfig, Module module) {
    try {
        // 从 XML 配置中获取 Activator 类名
        String attribute = moduleConfig.getAttribute(ModuleAttribute.Activator);

        // 通过反射创建 Activator 实例
        FineModule fineModuleCreate = FineModule.create(
            moduleConfig.getName(),
            createActivator(attribute),  // 反射实例化 Activator
            module,
            moduleConfig.getAttributes()
        );

        // 递归构建子模块
        buildChildren(fineModuleCreate, moduleConfig);  // ◄── 触发递归
        return fineModuleCreate;
    } catch (ClassNotFoundException e) {
        FineLoggerFactory.getLogger().debug("Module not found {}", moduleConfig.getName());
        return null;
    }
}

// 递归构建子模块
private static void buildChildren(FineModule fineModule, ModuleConfig moduleConfig) {
    for (ModuleConfig sub : moduleConfig.getSubs()) {
        FineModule child = build(sub, fineModule);  // ◄── 递归调用自己
        if (child != null) {
            fineModule.getSubRepo().add(child);
            child.setParent(fineModule);
        }
    }
}

private static Activator createActivator(String str) throws ... {
    if (StringUtils.isBlank(str)) {
        return new VirtualActivator();
    }
    // 通过类名反射创建实例
    return (Activator) Class.forName(str).newInstance();
}
```

**递归构建示意**:

```
build(server-startup.xml)           ← 第1次调用
    │
    └── buildChildren()
            │
            ├── build(server-start-basic.xml)   ← 第2次调用
            │       └── buildChildren()
            │               ├── build(basic.xml)            ← 第3次
            │               ├── build(core.xml)             ← 第4次
            │               └── build(function-base.xml)    ← 第5次
            │
            ├── build(server.xml)                 ← 第N次调用
            │       └── buildChildren()
            │               ├── build(decision.xml)
            │               │       └── build(DecisionServletInitializer)
            │               └── build(ReportActivator)
            │
            └── build(server-deployment-only.xml)
```

每个 XML 元素对应一次 `build()` 调用，整个启动过程可能调用几十次甚至上百次。

### 3.3 模块启动策略

#### 3.3.1 默认策略是 ParentFirstStrategy

**证据 1**: `ModuleAttribute.java:13`

```java
InvokeSubsStrategy("invoke-subs-strategy", "parent-first"),  // 默认值是 "parent-first"
```

**证据 2**: `InvokeSubStrategyFactory.java:49-59`

```java
public static InvokeSubStrategy create(String str) {
    if (StringUtils.isEmpty(str)) {
        return PARENT_FIRST.create();  // 空参数时返回 ParentFirstStrategy
    }
    for (InvokeSubStrategyFactory factory : values()) {
        if (StringUtils.equals(factory.tag, str)) {
            return factory.create();
        }
    }
    return ERROR.create();
}
```

**证据 3**: `FineModule.java:92`

```java
// 从模块属性中获取策略配置，如果没有配置则使用默认值
this.invokeSubStrategy = InvokeSubStrategyFactory.create(getAttribute(ModuleAttribute.InvokeSubsStrategy));
```

#### 3.3.2 可用策略列表

| 策略名 | XML 配置值 | 类 | 启动顺序 |
|--------|-----------|-----|---------|
| ParentFirst（默认） | `parent-first` 或空 | `ParentFirstStrategy` | 先启动父模块，再启动子模块 |
| SubsFirst | `subs-first` | `SubFirstStrategy` | 先启动子模块，再启动父模块 |
| Parallel | `parallel` | `ParallelStrategy` | 并行启动所有模块 |
| Custom | `custom` | `CustomStrategy` | 自定义启动顺序 |

#### 3.3.3 ParentFirstStrategy 实现逻辑

**源码位置**: `com/fr/module/engine/strategy/ParentFirstStrategy.java`

```java
@Override
public void doStart(Activator activator, List<Module> list) {
    // 先启动当前模块（父模块）
    activator.start();
    // 再启动所有子模块
    for (Module sub : list) {
        startSub(sub);
    }
}
```

#### 3.3.4 SubsFirstStrategy 实现逻辑

**源码位置**: `com/fr/module/engine/strategy/SubFirstStrategy.java:17-23`

```java
@Override
public void doStart(Activator activator, List<Module> list) {
    // 先启动所有子模块
    for (Module sub : list) {
        startSub(sub);
    }
    // 最后启动当前模块（父模块）
    activator.start();
}
```

#### 3.3.5 如何指定策略

在 XML 配置中通过 `invoke-subs-strategy` 属性指定：

```xml
<!-- 使用 subs-first 策略 -->
<report activator="com.fr.report.ReportActivator" invoke-subs-strategy="subs-first">
    <!-- 子模块 -->
</report>

<!-- 使用 parallel 策略 -->
<decision activator="com.fr.decision.DecisionActivator" invoke-subs-strategy="parallel">
    <!-- 子模块 -->
</decision>

<!-- 不指定则使用默认策略 parent-first -->
<server activator="com.fr.stable.web.ServerActivator">
    <!-- 子模块 -->
</server>
```

## 四、Servlet 动态注册机制

### 4.1 两种 Servlet 注册方式

帆软使用两种方式注册 Servlet：

| Servlet | 注册位置 | 作用 |
|---------|---------|------|
| `FineDispatcherServlet` | `DecisionServletInitializer` | 处理 `/decision/*` 请求 |
| `ReportServlet` | `ReportActivator` | 处理 `/ReportServer` 请求 |

### 4.2 DecisionServletInitializer 注册流程

**源码位置**: `com/fr/decision/base/DecisionServletInitializer.java:69-94`

```java
@Override
public void start() {
    ServletContext servletContext = (ServletContext) getModule().upFindSingleton(ServletContext.class);
    if (servletContext == null) {
        return;
    }

    AnnotationConfigWebApplicationContext springContext =
        (AnnotationConfigWebApplicationContext) getModule().upFindSingleton(AnnotationConfigWebApplicationContext.class);

    // 1. 注册 Spring 配置类
    springContext.register(DecisionHandlerAdapter.class);

    // 2. 添加各种 Filter
    addAntiCrashFilter(servletContext);
    addURIFilter(servletContext);
    addSecurityRefererFilter(servletContext);
    addSecurityHeaderFilter(servletContext);
    addEncryptionFilter(servletContext);

    // 3. 注册核心 DispatcherServlet
    PlatformScaffoldServletStarter.startServlet(
        servletContext,
        new FineDispatcherServlet(springContext)
    );

    // 4. 注册更多 Filter
    addCounterFilter(servletContext);
    addPluginStoreFilter(servletContext);
    addTenantFilter(servletContext);
    addResourceFilter(servletContext);

    // 5. 注册 Spring MVC 配置
    springContext.register(DecisionConfiguration.class);
}
```

### 4.3 ReportActivator 注册 ReportServlet

**源码位置**: `com/fr/report/ReportActivator.java:178-191`

```java
private void initReportServlet() {
    // 从模块上下文获取 ServletContext
    ServletContext servletContext = (ServletContext) getModule().upFindSingleton(ServletContext.class);
    if (servletContext == null) {
        return;
    }

    AnnotationConfigWebApplicationContext springContext =
        (AnnotationConfigWebApplicationContext) getModule().upFindSingleton(AnnotationConfigWebApplicationContext.class);

    // 动态注册 ReportServer Servlet
    ServletRegistration.Dynamic dynamicAddServlet = servletContext.addServlet(
        ServerConfig.getInstance().getReportServletName(),  // 默认 "ReportServer"
        new ReportServlet()
    );

    if (dynamicAddServlet != null) {
        // 配置 URL 映射
        dynamicAddServlet.addMapping(new String[]{
            ServerConfig.getInstance().getReportServletMapping()  // 默认 "/ReportServer"
        });
        // 设置启动顺序
        dynamicAddServlet.setLoadOnStartup(0);
        // 支持异步请求
        dynamicAddServlet.setAsyncSupported(true);
    }

    // 注册 Spring 配置
    springContext.register(ReportConfiguration.class);
}
```

### 4.4 Servlet 配置读取

**源码位置**: `com/fr/base/ServerConfig.java`

```java
// Servlet 名称（默认 "ReportServer"）
public String getReportServletName() {
    return "ReportServer";
}

// Servlet 映射路径（默认 "/ReportServer"）
public String getReportServletMapping() {
    return "/ReportServer";
}

// Decision Servlet 名称（默认 "decision"）
public String getServletName() {
    return "decision";
}

// Decision Servlet 映射（默认 "/decision/*"）
public String getServletMapping() {
    return "/" + getServletName() + "/*";
}
```

## 五、URL 路由机制

### 5.1 两个入口统一处理

| URL | 处理 Servlet | 最终处理 |
|-----|-------------|---------|
| `/webroot/ReportServer` | `ReportServlet` | `ReportDispatcher.dealWithRequest()` |
| `/webroot/decision/view/report` | `FineDispatcherServlet` → `ReportRequestService` | `ReportDispatcher.dealWithRequest()` |

### 5.2 Spring MVC 路由配置

**源码位置**: `com/fr/web/controller/ViewRequestConstants.java`

```java
// 主路由
public static final String REPORT_VIEW_PATH = "/view/report";

// 兼容路由（旧版本）
public static final String REPORT_VIEW_PATH_COMPATIBLE = "/view/ReportServer";
```

**ReportRequestService**（主路由）:

```java
@RequestMapping({ViewRequestConstants.REPORT_VIEW_PATH})  // /view/report
@Controller
public class ReportRequestService extends BaseRequestService {

    @RequestMapping({WebConstants.ALL_ALL})
    public void preview(HttpServletRequest request, HttpServletResponse response) throws Exception {
        handle(request, response);  // 调用 ReportDispatcher.dealWithRequest()
    }
}
```

**ReportRequestCompatibleService**（兼容路由）:

```java
@RequestMapping({ViewRequestConstants.REPORT_VIEW_PATH_COMPATIBLE})  // /view/ReportServer
@Controller
public class ReportRequestCompatibleService extends BaseRequestService {

    @RequestMapping({WebConstants.ALL_ALL})
    public void preview(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 重定向到新路由
        response.sendRedirect("${fineServletURL}" + REPORT_VIEW_PATH + "?" + request.getQueryString());
    }
}
```

## 六、模块化设计优势

### 6.1 核心概念

```
┌─────────────────────────────────────────────────────────────────┐
│                        模块化架构                                │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  模块 (Module)                                                  │
│    ├── 名称 (name)                                              │
│    ├── 激活器 (Activator) - 管理模块生命周期                      │
│    ├── 配置 (XML) - 定义模块结构和依赖                            │
│    └── 子模块 (Sub-modules) - 形成层次结构                        │
│                                                                 │
│  激活器 (Activator)                                             │
│    ├── start() - 模块启动时执行                                  │
│    │     ├── 注册 Servlet                                       │
│    │     ├── 注册 Filter                                        │
│    │     ├── 初始化资源                                         │
│    │     └── 注册服务                                           │
│    └── stop() - 模块停止时执行                                   │
│          ├── 清理资源                                           │
│          └── 注销服务                                           │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 6.2 设计优势

| 特性 | 传统方式 | 模块化方式 |
|------|---------|-----------|
| **配置管理** | 单一 web.xml，难以维护 | XML 模块配置，层次清晰 |
| **启动控制** | 所有组件同时加载 | 按需加载，可控启动顺序 |
| **扩展性** | 修改代码 | 添加模块配置 |
| **隔离性** | 组件间耦合 | 模块相对独立 |
| **生命周期** | 容器管理 | 模块自管理（start/stop） |

### 6.3 关键设计模式

1. **激活器模式 (Activator Pattern)**
   - 每个模块有一个 Activator 负责生命周期管理
   - `start()` 初始化资源，`stop()` 清理资源

2. **策略模式 (Strategy Pattern)**
   - `SubFirstStrategy`: 先启动子模块
   - `ParentFirstStrategy`: 先启动父模块
   - `ParallelStrategy`: 并行启动

3. **依赖注入 (Dependency Injection)**
   - 通过 `getModule().upFindSingleton()` 获取共享资源
   - `setSingleton()` 注册共享资源

## 七、总结

帆软的模块化架构通过以下机制实现：

1. **配置驱动**: XML 文件定义模块结构和依赖关系
2. **反射实例化**: 运行时动态创建 Activator 实例
3. **生命周期管理**: Activator 的 `start()`/`stop()` 管理资源
4. **动态注册**: 使用 ServletContext API 动态注册 Servlet 和 Filter
5. **统一入口**: 最终所有请求汇聚到 `ReportDispatcher.dealWithRequest()` 处理

这种设计使得帆软可以灵活地组合不同功能模块，支持按需加载和热插拔，便于功能扩展和维护。
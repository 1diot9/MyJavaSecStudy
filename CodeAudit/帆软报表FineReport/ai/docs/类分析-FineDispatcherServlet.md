# FineDispatcherServlet 类分析

> **源码路径**: `com/fr/decision/webservice/servlet/FineDispatcherServlet.java`

---

## 一、类定义

```java
public class FineDispatcherServlet extends DispatcherServlet {
    public FineDispatcherServlet(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
    }

    @Override
    protected void doDispatch(HttpServletRequest httpServletRequest,
                              HttpServletResponse httpServletResponse) throws Exception {
        try {
            super.doDispatch(httpServletRequest, httpServletResponse);
        } catch (InterruptedException e) {
            throw ThreadAlarmException.createDefaultInterruptedException();
        }
    }
}
```

---

## 二、继承关系

```
javax.servlet.http.HttpServlet
    └── org.springframework.web.servlet.HttpServletBean
            └── org.springframework.web.servlet.FrameworkServlet
                    └── org.springframework.web.servlet.DispatcherServlet
                            └── com.fr.decision.webservice.servlet.FineDispatcherServlet
```

**关键点**：
- 继承自 Spring MVC 的 `DispatcherServlet`（位于 `com.fr.third.springframework.web.servlet` 包）
- 是 Spring MVC 前端控制器的定制实现

---

## 三、Servlet 注册过程

### 3.1 注册入口

在 `DecisionServletInitializer.start()` 方法中注册：

```java
// DecisionServletInitializer.java:83
PlatformScaffoldServletStarter.startServlet(servletContext,
    new FineDispatcherServlet(annotationConfigWebApplicationContext));
```

### 3.2 注册逻辑

```java
// PlatformScaffoldServletStarter.java:23-28
public static void startServlet(ServletContext servletContext, Servlet dispatcherServlet) {
    ServletRegistration.Dynamic servlet = servletContext.addServlet(
        PlatformScaffold.getBasicConfigProvider().getServletName(),  // "decision"
        dispatcherServlet
    );
    servlet.addMapping(new String[]{
        PlatformScaffold.getBasicConfigProvider().getServletMapping()  // "/decision/*"
    });
    servlet.setLoadOnStartup(1);
    servlet.setAsyncSupported(true);
}
```

### 3.3 Servlet 名称来源

```java
// PlatformScaffoldBasicConfigProvider.java:38-44
default String getServletName() {
    return "decision";
}

default String getServletMapping() {
    return "/" + getServletName() + "/*";
}
```

**注册结果**：
- Servlet 名称: `decision`
- URL 映射: `/decision/*`

---

## 四、核心功能

### 4.1 前端控制器 (Front Controller)

`FineDispatcherServlet` 是 FineReport 应用的**核心前端控制器**，负责：

1. **请求分发**: 接收所有 `/decision/*` 路径的 HTTP 请求
2. **路由匹配**: 通过 Spring MVC 的 `HandlerMapping` 找到对应的 Controller
3. **拦截器执行**: 按顺序执行配置的拦截器链
4. **视图渲染**: 处理响应数据的渲染

### 4.2 请求处理流程

```
HTTP 请求 (/webroot/decision/login)
        │
        ▼
┌─────────────────────────────────────────────────────────────┐
│                    Tomcat 容器                               │
│  ① 根据 Context Path (/webroot) 找到应用                    │
│  ② 根据 Servlet Mapping (/decision/*) 找到 Servlet          │
└─────────────────────────────────────────────────────────────┘
        │
        ▼
┌─────────────────────────────────────────────────────────────┐
│                  FineDispatcherServlet                       │
│                                                              │
│  ③ doService() → doDispatch()                              │
│                                                              │
│  ④ 执行 Filter 链 (在 Servlet 之前):                        │
│     ┌─────────────────────────────────────────────────┐    │
│     │ AntiCrashFilter        → 防崩溃                  │    │
│     │ CharacterEncodingFilter → 字符编码 (UTF-8)       │    │
│     │ URICheckFilter         → URI 安全检查            │    │
│     │ SecurityRefererFilter  → Referer 检查            │    │
│     │ SecurityHeaderFilter   → 安全头设置              │    │
│     │ EncryptionLevelFilter  → 加密级别                │    │
│     │ TenantFilter           → 多租户处理              │    │
│     │ ...更多 Filter...                               │    │
│     └─────────────────────────────────────────────────┘    │
│                                                              │
│  ⑤ 执行 Interceptor 链 (在 Controller 之前):                │
│     ┌─────────────────────────────────────────────────┐    │
│     │ WebServiceThreadInterceptor                      │    │
│     │ WhiteListThreadInterceptor                       │    │
│     │ SystemAvailableInterceptor  → 系统可用性         │    │
│     │ MigrationInterceptor        → 迁移处理           │    │
│     │ EncryptionInterceptor       → 加密处理           │    │
│     │ RequestPreHandleInterceptor → 请求预处理         │    │
│     │ DecisionInterceptor         → 决策核心           │    │
│     │ SecurityAccessInterceptor   → 安全访问控制       │    │
│     │ SessionCheckInterceptor     → 会话检查           │    │
│     │ URLRateLimiterInterceptor   → 限流               │    │
│     │ ...更多 Interceptor...                          │    │
│     └─────────────────────────────────────────────────┘    │
│                                                              │
│  ⑥ HandlerMapping 查找匹配的 Controller 方法                │
│                                                              │
│  ⑦ 执行 Controller 业务逻辑                                 │
│                                                              │
│  ⑧ 视图渲染 / 返回响应                                      │
│                                                              │
└─────────────────────────────────────────────────────────────┘
        │
        ▼
    HTTP 响应
```

### 4.3 自定义扩展

相比 Spring 原生的 `DispatcherServlet`，`FineDispatcherServlet` 仅增加了：

```java
// 对线程中断异常的特殊处理
catch (InterruptedException e) {
    throw ThreadAlarmException.createDefaultInterruptedException();
}
```

**目的**: 将 `InterruptedException` 转换为 `ThreadAlarmException`，用于线程告警机制。

---

## 五、关联配置类

### 5.1 DecisionConfiguration

路径: `com/fr/decision/base/DecisionConfiguration.java`

```java
@EnableAspectJAutoProxy
@EnableWebMvc
@Configuration
@ComponentScan
public class DecisionConfiguration extends FineWebApplicationConfiguration {
    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        // 添加各种拦截器...
    }
}
```

**功能**:
- 配置 Spring MVC 环境
- 注册拦截器链
- 配置 AOP 切面（日志、权限、安全审计等）

### 5.2 DecisionServletInitializer

路径: `com/fr/decision/base/DecisionServletInitializer.java`

**功能**:
- 初始化 Filter 链
- 注册 `FineDispatcherServlet`
- 配置 GZIP 压缩
- 初始化全局状态变量

---

## 六、安全审计关注点

### 6.1 Filter 链入口

`FineDispatcherServlet` 之前的 Filter 链是安全审计的重点：

| Filter | 功能 | 安全关注点 |
|--------|------|-----------|
| `AntiCrashFilter` | 防崩溃 | 异常处理逻辑 |
| `URICheckFilter` | URI 检查 | 路径穿越、编码绕过 |
| `SecurityRefererFilter` | Referer 检查 | CSRF 防护 |
| `SecurityHeaderFilter` | 安全头 | 响应头配置 |
| `EncryptionLevelFilter` | 加密级别 | 加密算法安全性 |
| `TenantFilter` | 多租户 | 租户隔离绕过 |

### 6.2 Interceptor 链入口

`FineDispatcherServlet` 内部的拦截器链：

| Interceptor | 功能 | 安全关注点 |
|-------------|------|-----------|
| `SystemAvailableInterceptor` | 系统可用性 | 状态泄露 |
| `DecisionInterceptor` | 决策核心 | 认证绕过 |
| `SecurityAccessInterceptor` | 安全访问 | 权限绕过 |
| `SessionCheckInterceptor` | 会话检查 | 会话固定、劫持 |
| `URLRateLimiterInterceptor` | 限流 | 限流绕过 |

### 6.3 反序列化风险

`FineDispatcherServlet` 作为请求入口，所有反序列化操作都在其处理流程中触发：

- 请求体解析
- 参数绑定
- 对象反序列化

---

## 七、总结

| 属性 | 值 |
|------|-----|
| **类名** | `FineDispatcherServlet` |
| **继承** | `DispatcherServlet` (Spring MVC) |
| **Servlet 名称** | `decision` |
| **URL 映射** | `/decision/*` |
| **核心职责** | 前端控制器，分发所有业务请求 |
| **自定义扩展** | `InterruptedException` → `ThreadAlarmException` |
| **安全意义** | 请求入口，所有攻击面分析起点 |
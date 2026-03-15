路由接口在fine-report-engine-11.0.jar的com.fr.nx.app.web.controller包中里面定义了一系列路由



![](./assets/8356220198939095256.jpg)





参数解析在com.fr.nx.app.web.v9.handler.handler.largeds.LargeDatasetExcelExportHandler#initCreator方法中



第一个参数__parameters__，格式为json格式，使用get,header方式传递参数



![](./assets/2524331385353982899.jpg)





进入LargeDatasetExcelExportJavaScript var7 = this.getEntity(var2);



第二个参数params，可以看见params参数为一个xml格式的



![](./assets/4086173259271444572.jpg)





编写相关函数生成一个xml



![](./assets/2992958340366001881.jpg)





进入到this.dealParam方法



![](./assets/4220974768581276585.jpg)





第三个参数functionParams，格式为json格式



![](./assets/5284950823409808460.jpg)





俩种情况：



一是可以构建一个Formula对象走第一个var2.evalValue，二是不构建Formula对象，使用占位符来替换走第二个var2.evalValue



![](./assets/7936316859095728720.jpg)





构建相关参数，成功传入



![](./assets/2754539711534229895.jpg)





evalValue如何解析表达式的？



 `FRParser` 是 FineReport 的核心表达式解析器，使用 ANTLR 生成的递归下降解析器（Recursive Descent Parser）来解析 FineReport 表达式语言。



支持com.fr.function包下面的如下函数调用



![](./assets/6644844542789506692.jpg)





如何执行SQL语句 下面是调用栈





```plain

at com.fr.data.core.db.dialect.base.key.create.executequery.DialectExecuteSecurityQueryKey.execute(DialectExecuteSecurityQueryKey.java:20)
at com.fr.data.core.db.dialect.base.key.create.executequery.DialectExecuteSecurityQueryKey.execute(DialectExecuteSecurityQueryKey.java:13)
at com.fr.data.core.db.dialect.AbstractDialect.execute(AbstractDialect.java:123)
at com.fr.data.core.db.dialect.DefaultDialect.executeQuery(DefaultDialect.java:670)
at com.fr.data.impl.restriction.DBQueryTimeoutExecution.execute(DBQueryTimeoutExecution.java:-1)
at com.fr.data.impl.restriction.DBQueryTimeoutExecution.execute(DBQueryTimeoutExecution.java:-1)
at com.fr.data.impl.restriction.TimeoutExecutor$1.call(TimeoutExecutor.java:-1)
at java.util.concurrent.FutureTask.run$$$capture(FutureTask.java:266)
at java.util.concurrent.FutureTask.run(FutureTask.java:-1)
 - 异步堆栈跟踪
at java.util.concurrent.FutureTask.<init>(FutureTask.java:132)
at com.fr.data.impl.restriction.TimeoutExecutor.execute(TimeoutExecutor.java:-1)
at com.fr.data.impl.AbstractDBDataModel.executeQuery$original$vMZXKr0a(AbstractDBDataModel.java:-1)
at com.fr.data.impl.AbstractDBDataModel.executeQuery$original$vMZXKr0a$accessor$sNZDFzwe(AbstractDBDataModel.java:-1)
at com.fr.data.impl.AbstractDBDataModel$auxiliary$uC40GCH2.call(Unknown Source:-1)
at com.fr.tolerance.FaultToleranceProcess$1.execute(FaultToleranceProcess.java:-1)
at com.fr.tolerance.FaultToleranceInterceptor.interceptor(FaultToleranceInterceptor.java:-1)
at com.fr.data.impl.AbstractDBDataModel.executeQuery(AbstractDBDataModel.java:-1)
at com.fr.data.impl.AbstractDBDataModel.timeoutExecuteQuery(AbstractDBDataModel.java:-1)
at com.fr.data.impl.AbstractDBDataModel.initConnectionAndResultAndCheckInColumns(AbstractDBDataModel.java:-1)
at com.fr.data.impl.AbstractDBDataModel.iterateResultSet(AbstractDBDataModel.java:-1)
at com.fr.data.impl.AbstractDBDataModel.iterateResultSet(AbstractDBDataModel.java:-1)
at com.fr.data.impl.AbstractCacheDBDataModel.hasRow(AbstractCacheDBDataModel.java:-1)
at com.fr.data.impl.MemCachedDBDataModel.getValueAt(MemCachedDBDataModel.java:-1)
at com.fr.function.SQL.run(SQL.java:-1)
at com.fr.script.AbstractFunction.tryRun(AbstractFunction.java:-1)
at com.fr.script.AbstractFunction.evalExpression(AbstractFunction.java:-1)
at com.fr.parser.FunctionCall.eval(FunctionCall.java:-1)
at com.fr.script.Calculator.eval(Calculator.java:-1)
at com.fr.stable.script.Expression.eval(Expression.java:35)
at com.fr.script.Calculator.evalString(Calculator.java:-1)
at com.fr.script.Calculator.eval(Calculator.java:-1)
at com.fr.script.Calculator.evalValue(Calculator.java:-1)
at com.fr.nx.app.web.v9.handler.handler.largeds.LargeDatasetExcelExportHandler.dealParam(LargeDatasetExcelExportHandler.java:147)
at com.fr.nx.app.web.v9.handler.handler.largeds.LargeDatasetExcelExportHandler.initCreator(LargeDatasetExcelExportHandler.java:99)
at com.fr.nx.app.web.v9.handler.handler.largeds.LargeDatasetExcelExportHandler.doHandle(LargeDatasetExcelExportHandler.java:209)
at com.fr.nx.app.direct.AbstractExportHandler.handleRequest(AbstractExportHandler.java:22)
at com.fr.web.controller.AbstractRequestService.handle(AbstractRequestService.java:49)
at com.fr.web.controller.AbstractRequestService.handle(AbstractRequestService.java:28)
at com.fr.web.handler.AbstractReportRequestHandler.handle(AbstractReportRequestHandler.java:26)
at com.fr.nx.app.web.controller.NXController.largedsExcelExportV9(NXController.java:320)
at sun.reflect.GeneratedMethodAccessor286.invoke(Unknown Source:-1)
at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
at java.lang.reflect.Method.invoke(Method.java:498)
at com.fr.third.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:205)
at com.fr.third.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:133)
at com.fr.third.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:97)
at com.fr.third.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:854)
at com.fr.third.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:765)
at com.fr.third.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:85)
at com.fr.third.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:967)
at com.fr.decision.webservice.servlet.FineDispatcherServlet.doDispatch(FineDispatcherServlet.java:29)
at com.fr.third.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:901)
at com.fr.third.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:970)
at com.fr.third.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:861)
at javax.servlet.http.HttpServlet.service(HttpServlet.java:529)
at com.fr.third.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:846)
at javax.servlet.http.HttpServlet.service(HttpServlet.java:623)
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:199)
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:144)
at com.fr.decision.webservice.BackupActivator$1.doFilter(BackupActivator.java:77)
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:168)
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:144)
at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:51)
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
at org.apache.catalina.valves.AbstractAccessLogValve.invoke(AbstractAccessLogValve.java:656)
at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74)
at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:346)
at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:397)
at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63)
at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:935)
at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1792)
at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52)
at org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1189)
at org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:658)
at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:63)
at java.lang.Thread.run(Thread.java:745)

```



SQL注入过滤，移除特殊字符



![](./assets/6853656359295885970.jpg)





SQL注入过滤，通过正则匹配关键词



![](./assets/8067391341803490861.jpg)





接下来就是SQLITE如何Getshell的问题了，这里已经能够执行SQL语句了



​




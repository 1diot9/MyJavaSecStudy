# Smartbi 官方漏洞公告整理

## 说明

- 数据来源：
  - 官方补丁规则文件 `patch.patches`
  - 代码与路由定义文件 `resources/xml/web.xml`、`resources/xml/applicationContext.xml` 及相关源码
- 归属原则：
  - 以官方公告标题为主键。
  - 以 `patch.patches` 中的补丁分组、路由、规则、类名、方法名作为主要证据。
- 置信度说明：
  - `高`：路由/方法名与补丁规则直接一致。
  - `中`：属于同一利用链，但只命中部分补丁点。
  - `低`：仅能确认属于同类漏洞，无法唯一绑定到某一公告。

## 官方公告逐项结果

### 2025-07-31 强化系统安全性，防范特定条件下的非授权访问5

- 补丁键：`PATCH_20250731`
- 路由摘要：
  - `/vision/share.jsp`
  - `/smartbix/api/agentEngineMonitor/*`
  - `/smartbix/api/customextension/customsqlnode/nodedefine`
  - `/smartbix/api/datamining/config/*`
  - `/smartbix/api/datamining/customExtensionNode/*`
  - `/smartbix/api/engineMonitor/*`
  - `/smartbix/api/serveMonitor/*`
  - `/smartbix/api/sparkUiMonitor/*`
  - `/smartbix/api/dataprepare/sparkfunctions/*`
  - `/smartbix/api/jobflow/config`
  - `/smartbix/api/jobflow/jobFlowLog*`
  - `/smartbix/api/user/scheduleIsPasswordValidate`
  - `/smartbix/api/user/isPasswordValidate`
  - `/smartbix/api/admin/clearCache`
  - `/smartbix/api/login`
  - `/smartbix/*`
- 规则摘要：
  - `ShareRecordPatchRule`
  - 大量 `AssertFunctionUrlPatchRule`
  - `RejectPatchRule`
  - `RejectInvalidUrlPatchRule`
- 结论：
  - 该补丁集中加固分享链路、管理接口和多个 `smartbix` API，属于典型的“多入口非授权访问”防护包。
  - `/vision/share.jsp` 是本次补丁中非常明确的关键入口。

### 2025-06-11 强化系统安全性，防范特定条件下的非授权访问4

- 补丁键：`PATCH_20250611`
- 路由摘要：
  - `/vision/RMIServlet`
  - `/smartbix/api/pages/beans`
  - `/smartbix/api/pages/refresh`
  - `/smartbix/api/pages/beans/*`
  - `/smartbix/api/augmentedQuery/javaSubQueryService/refreshDefine`
- 规则摘要：
  - 拒绝 `DefenderClientService.resetCache/clear/load/checkPath/checkCondition`
  - 仅管理员可访问 `DefenderClientService.loadConfig/checkConfigItem/checkPathAndCondition`
  - 拒绝 `MetricsModelForVModule.checkExpression`
  - `WindowUnLoadingAndAttributeRule`
  - `PageServicePatchRule`

### 2025-03-27 强化系统安全性，防范特定条件下的非授权访问3

- 补丁键：`PATCH_20250327`
- 路由摘要：
  - `/vision/RMIServlet`
  - `/vision/SyncServlet`
  - `/workflow/*`
- 规则摘要：
  - 拒绝 `DataSourceService.createMetricDatasource/createScriptDataSource/createUnionDatasource/simpleTestConnection` 中出现 `clientRerouteServerListJNDIName`
  - `SyncServlet` 仅管理员可用，并禁止 `dbServer/dbName` 参数携带 `clientRerouteServerListJNDIName`
  - `WorkflowPatchRule`
- 说明：
  - 该公告继续收敛 DB2 JNDI 相关利用面，并把限制从 RMI 扩展到同步与工作流相关入口。

### 2024-08-08 强化系统安全性，防范特定条件下的非授权访问2

- 补丁键：`PATCH_20250107`（补丁描述内日期为 `Patch.20240808`）
- 路由摘要：
  - `/vision/RMIServlet`
- 规则摘要：
  - `AugmentedDataSetForVModule.getProcQueryVO` 需 `AUGMENTED_DATASET_PROC`
  - `AugmentedDataSetForVModule.getJsQueryVO/checkJavaScript` 需 `AUGMENTED_DATASET_JS`
  - `AugmentedDataSetForVModule.getJavaQueryVO` 需 `AUGMENTED_DATASET_JAVA`
  - `AugmentedDataSetForVModule.getSQLQueryVO/checkSql` 被直接拒绝
  - 拒绝 `SpreadsheetReportModule.getImageBase64`
  - 拒绝 `ChartService.getImageBase64`
  - 拒绝部分 `BIConfigService/SessionLogService/ScheduleQueryModule` 调用
  - `UserService.assignRolesToDepartment/assignRolesToGroup` 增加权限校验
- 结论：
  - 这是 `AugmentedDataSetForVModule` 相关危险能力的一次集中收敛，重点是脚本、Java、SQL、存储过程查询能力的功能权限化和部分高风险接口的直接拒绝。

### 2024-06-27 强化系统安全性，防范特定条件下的非授权访问

- 补丁键：`PATCH_20240627`
- 路由摘要：
  - `/smartbix/api/augmentedQuery/jsSubQuery/gridData/*`
  - `/smartbix/api/augmentedQuery/jsSubQuery/checkJavaScript`
  - `/smartbix/api/augmentedQuery/jsSubQuery/refreshDefine`
  - `/vision/heart.jsp`
- 规则摘要：
  - JS 子查询相关接口统一增加 `AUGMENTED_DATASET_JS` 权限
  - `heart.jsp?autoLog=1` 仅允许配置用户或管理员访问

### 2024-04-18 修复某种特定情况下越权漏洞

- 补丁键：`PATCH_20240418`
- 路由摘要：
  - `/vision/RMIServlet`
  - `/smartbix/api/monitor/setServiceAddress`
  - `/smartbix/api/monitor/setEngineAddress`
  - `/smartbix/api/monitor/setEngineInfo`
- 规则摘要：
  - 全局拒绝 `GET/FormData` 方式打到 `RMIServlet`
  - 拒绝 `StateService.setCurrentUser`
  - 拒绝 `ScheduleQueryModule.executeQuery`
  - 拒绝 `SocialContactShareService.getCode/getSiteShareRecordById`
  - 拒绝 `DataSourceService` 的 Java 查询/Java 数据源相关参数
  - 拒绝多个 `smartbix/api/monitor/set*` 地址设置接口

### 2023-08-22 修复某种特定情况下登录与权限验证漏洞

- 补丁键：`PATCH_20230822`
- 路由摘要：
  - `/vision/RMIServlet`
- 规则摘要：
  - `WindowUnLoadingAndAttributeRule`
  - `ScheduleSDK.testSelfDefineTask/testCustomTask` 增加权限校验
  - 拒绝 `UserService.assignRolesToUserInner`
- 对应理由：
  - 该公告重点是 `windowUnloading` 类入口收敛和高风险调度能力的补充鉴权，主题与“登录与权限验证漏洞”标题一致。

### 2023-08-08 修复特定场景下设置Token回调地址漏洞

- 补丁键：`PATCH_20230808`
- 路由摘要：
  - `/smartbix/api/monitor/setAddress`
  - `/smartbix/api/excelandsheet/uploadfile`
- 规则摘要：
  - 完全拒绝 `setAddress`
  - 禁止公共用户访问上传接口

### 2023-08-04 修复在某种特定情况下任意文件上传和删除的漏洞

- 补丁键：`PATCH_20230804`
- 路由摘要：
  - `/vision/RMIServlet`
  - `/smartbix/api/customextension/customExtensionNode/*`
  - `/smartbix/api/datamining/upload/modelnode`
  - `/smartbix/api/excelandsheet/uploadfile`
  - `/smartbix/api/autotuning/autotunning/model`
  - `/smartbix/api/datamining/model`
  - `/smartbix/api/datamining/modelfile/download`
  - `/smartbix/api/datamining/dagInstance/*`
  - `/smartbix/api/datamining/cleanCache/*`
  - `/smartbix/api/jobflow/jobflowinstance/*`
  - `/smartbix/api/datamining/customExtensionNode/*`
- 规则摘要：
  - 在 `DataSourceService` 多个方法中拦截 `traceFile`
  - 多个 `smartbix` 上传/下载/任务接口增加相对路径校验

### 2023-07-28 修复在某种特定情况下破解用户密码和特定情况下DB2绕过判断执行命令漏洞

- 补丁键：`PATCH_20230728`
- 路由摘要：
  - `/vision/RMIServlet`
- 规则摘要：
  - `UserService.getPassword` 限制为仅能获取自身密码
  - `CommonService.getSessionAttribute`、`StateService.getSessionAttribute` 限制敏感会话属性
  - 拒绝 `ManagementService.getMonitorMap`
  - 拒绝 `MigrateModule.getExportLog`
  - 拒绝 `BIConfigService.loadConfigNoAuth`
  - 拒绝 `CommonService.getFolderPaths`
  - `DataSourceService` 多个方法拦截 `clientRerouteServerListJNDIName`
- 结论：
  - 该公告同时覆盖口令获取、敏感会话读取和 DB2 JNDI 载荷过滤，是一个典型的多问题合并修补包。

### 2023-07-14 修复在某种特定情况下修改用户密码漏洞

- 补丁键：`PATCH_20230714`
- 路由摘要：
  - `/vision/RMIServlet`
- 规则摘要：
  - 拒绝 `UserService.changePasswordEx`

### 2023-07-03 修复登录代码逻辑漏洞

- 补丁键：`PATCH_20230703`
- 路由摘要：
  - 无
- 规则摘要：
  - `patch.patches` 中未记录路由级规则，推测是代码逻辑修复

### 2023-06-12 修复在某种特定情况下默认用户绕过登录漏洞

- 补丁键：`PATCH_20230612`
- 路由摘要：
  - `/vision/RMIServlet`
- 规则摘要：
  - 拒绝 `UserService.loginFromDB`

### 2023-05-30 修复了HSQL任意文件读取漏洞

- 补丁键：`PATCH_20230530`
- 路由摘要：
  - `/vision/RMIServlet`
- 规则摘要：
  - `DataSourceService.testConnectionList/testConnection/createDataSource/updateDataSource/updateDataSourceWithUnionDB` 统一使用 `RejectRMIDataConnPatchRule`

### 2023-02-28 修复了利用stub接口对"DB2 命令执行漏洞"补丁进行绕过的远程命令执行漏洞

- 补丁键：`PATCH_20230228`
- 路由摘要：
  - `*.stub`
- 规则摘要：
  - `RejectStubPostPatchRule`
- 结论：
  - 该公告的核心是封堵 `.stub` 入口对既有 DB2/JNDI 补丁的绕过利用。

### 2022-11-22 修复了 DB2 命令执行漏洞

- 补丁键：`PATCH_20221122`
- 路由摘要：
  - `/vision/RMIServlet`
- 规则摘要：
  - `DataSourceService.testConnectionList/testConnection` 拦截 `clientRerouteServerListJNDIName`
- 说明：
  - 这是 DB2 JNDI/RCE 利用链中的较早补丁点，主要针对 `RMIServlet` 上的数据源连接测试能力做参数拦截。

### 2022-08-10 修复了任意文件上传、代码执行、未授权登录等漏洞

- 补丁键：`PATCH_20220810`
- 路由摘要：
  - `*.stub`
  - `/vision/RMIServlet`
- 规则摘要：
  - 阻断 `*.stub` POST
  - `BIConfigService` 仅允许配置/管理员访问
  - 拒绝 `UserService.pushLoginTokenByEngine`
  - `ScheduleSDK.testSelfDefineTask` 增加权限校验
  - `DataSourceService.testConnection/testConnectionList` 拦截 `loggerFile`
- 结论：
  - 这是一次覆盖上传、执行与未授权登录等多个高危面的综合性补丁。

### 2021-12-21 修复了Log4j2的DoS漏洞

- 补丁键：`PATCH_20211221`
- 路由摘要：
  - `/Log4j2RemoveJNDI`
- 规则摘要：
  - `RemoveLog4j2JNDIPatchRule`

### 2021-12-10 修复了Log4j2的JNDI漏洞

- 补丁键：`PATCH_20211210`
- 路由摘要：
  - `/Log4j2RemoveJNDI`
- 规则摘要：
  - `RemoveLog4j2JNDIPatchRule`

### 2021-08-24 修复脚本注入漏洞

- 补丁键：`PATCH_20210824`
- 路由摘要：
  - `/smartbix/api/pages/refresh/*`
- 规则摘要：
  - `EscapeRefreshString`

### 2021-07-16 修复了产品登录验证的漏洞

- 补丁键：`PATCH_20210716`
- 路由摘要：
  - `/vision/RMIServlet`
- 规则摘要：
  - `RejectRMIEncodeRule`
- 说明：
  - 该公告主要对应编码参数与登录验证相关的历史问题，是后续 `windowUnloading` 类问题修复链条中的前序补丁之一。

### 2021-04-29 修复imageimport.jsp存在任意文件上传漏洞

- 对应补丁键：
  - `PATCH_20210428`
- 路由摘要：
  - `/vision/designer/imageimport.jsp`
  - `/vision/ImageImportServlet`
- 规则摘要：
  - 两个入口直接返回 404
- 说明：
  - 官方公告日期为 2021-04-29，补丁内部日期为 2021-04-28。

### 2020-10-14 config界面的文件路径增加访问限制

- 补丁键：`PATCH_20201014`
- 路由摘要：
  - `/vision/chooser.jsp`
  - `/vision/RMIServlet`
- 规则摘要：
  - `chooser.jsp?config` 使用 `ChoosePathPatchRule`
  - `BIConfigService.saveConfig` 对 `configFileDir/licenseFileDir/logFileDir/extensionDirectory/dynamicLibraryPath` 做路径限制

### 2020-06-03 修复补丁包日期显示异常的问题

- 补丁键：`PATCH_20200603`
- 路由摘要：
  - 无
- 结论：
  - 这是补丁展示问题，不是漏洞修复。

### 2020-05-21 设置默认所有ip都能访问config页面

- `patch.patches` 状态：
  - 未找到对应补丁键
- 结论：
  - 该条从标题看更像配置变更，且安全性方向与“限制访问”相反，不适合作为漏洞修复项。

### 2020-04-20 兼容旧版本smartbi和支持WebLogic和WebSphere

- `patch.patches` 状态：
  - 未找到对应补丁键
- 结论：
  - 属于兼容性说明，不属于漏洞公告。

### 2020-03-25 修复越过登录查看和修改敏感系统配置信息，修改部分SQL注入

- 对应补丁键：
  - `PATCH_20200325`
- 路由摘要：
  - `/vision/debugtools.html`
  - `/vision/bMap/baiduMap.html`
  - `/vision/offline.html`
  - `/vision/MacroConsole.jsp`
  - `/vision/regeneratemacro.jsp`
  - `/vision/RMIServlet`
- 规则摘要：
  - 多个页面仅允许配置/管理员访问
  - `BIConfigService` 加管理权限
  - `ScheduleQueryModule.getNodesAndParent/searchElementsWithSchedules` 拦截单引号
  - `CombinedQueryService.updateWarnings` 拦截单引号

### 2020-03-07 修复XSS攻击漏洞

- `patch.patches` 状态：
  - 未发现同日期补丁键
  - 仅发现 `PATCH_20191218` 的 `XSS 攻击`
- 可关联路由：
  - `/vision/RMIServlet`
- 可关联规则：
  - `EscapeErrorDetailPatchRule`
- 说明：
  - 由于日期不一致，这里只做“同类项关联”，不做强绑定。

### 2019-12-12 修复奇安信通告中的 QTVA-2019-1261556、QTVA-2019-1261694、QTVA-2019-1258781

- 对应补丁键：
  - `PATCH_20191212`
- 路由摘要：
  - `/vision/RMIServlet`
- 规则摘要：
  - `BIConfigService.delUsers` 拦截逗号
  - 拒绝 `CommonService.saveToPropFile`
  - 拒绝 `CommonService.loadFromPropFile`

## 最终结论

- 已逐条保留原文中的官方公告、补丁键、路由、规则和分析说明。
- 当前文档适合作为精简后的官方公告清单版本。

# evo-apigw URL 路由对应表

本文档基于当前仓库中的 nginx 配置、`evo-apigw.jar` 反编译结果、以及各微服务 `application.properties` / `application-config.properties` / `module-dependence-config.json` 整理。

补充说明：

- 2026-04-06 已通过 `evo-discover` 实际访问 `GET /evo-discover/1.0.0/srd/subsystem` 拿到当前注册服务列表
- 因此，下面表格里新增了一批“discover 已确认注册，但外部 URL 前缀仍需区分是 apigw 转发还是 nginx 直转”的条目
- 如果某服务已经在 discover 中注册，且没有被 nginx 特殊拦截或改写，按 `CustomRouteLocator` 的主规则，通常可推导出 `/evo-apigw/<serviceName>/**` 这一类网关入口

重点说明：

- 外层 nginx 对 `/evo-apigw/` 使用 `proxy_pass https://hauc_backend/;`
- 因为 `proxy_pass` 带尾部 `/`，所以转发给 `8945` 时会去掉 `/evo-apigw/` 前缀
- `8945` 上运行的是 `evo-apigw` 网关服务
- `evo-apigw` 内部使用 `CustomRouteLocator` 动态生成路由
- 主路由规则是按服务名生成 `/服务名/**`
- discover 回调和定时检查会动态刷新路由

## 1. 通过 `/evo-apigw/` 进入网关的常见微服务

| 外部访问 URL 前缀 | 8945 实际收到的路径前缀 | 对应微服务 `system.name` | 微服务端口 | 说明 |
|---|---|---|---:|---|
| `/evo-apigw/evo-brm/**` | `/evo-brm/**` | `evo-brm` | `8918` | BRM 服务 |
| `/evo-apigw/evo-oauth/**` | `/evo-oauth/**` | `evo-oauth` | `8917` | OAuth/认证服务 |
| `/evo-apigw/evo-event/**` | `/evo-event/**` | `evo-event` | `8919` | 事件服务 |
| `/evo-apigw/evo-emap/**` | `/evo-emap/**` | `evo-emap` | `8920` | 地图/emap 服务 |
| `/evo-apigw/evo-cascade/**` | `/evo-cascade/**` | `evo-cascade` | `8948` | 级联服务 |
| `/evo-apigw/evo-dahua-open-api/**` | `/evo-dahua-open-api/**` | `evo-dahua-open-api` | `9141` | 开放接口服务 |
| `/evo-apigw/evo-job/**` | `/evo-job/**` | `evo-job` | `8915` | 定时任务服务 |
| `/evo-apigw/evo-doc/**` | `/evo-doc/**` | `evo-doc` | `9139` | 文档服务 |
| `/evo-apigw/evo-linkage/**` | `/evo-linkage/**` | `evo-linkage` | `8922` | 联动服务 |
| `/evo-apigw/evo-log/**` | `/evo-log/**` | `evo-log` | `8924` | 日志服务 |
| `/evo-apigw/evo-package/**` | `/evo-package/**` | `evo-package` | `8923` | 包管理服务 |
| `/evo-apigw/evo-mr/**` | `/evo-mr/**` | `evo-mr` | `9610` | MR 服务 |
| `/evo-apigw/evo-arsm/**` | `/evo-arsm/**` | `evo-arsm` | `8956` | discover 实际返回端口为 `8956` |
| `/evo-apigw/evo-oss/**` | `/evo-oss/**` | `evo-oss` | `8927` | discover 已注册；但项目中更常见的是 nginx 直接暴露 `/evo-oss/**` |
| `/evo-apigw/admin/**` | `/admin/**` | `admin` | `9407` | Evo-video admin 服务 |

## 1.1 discover 已确认注册，但外部前缀未在现有 nginx 规则中完全坐实的服务

这些服务已经能从 `GET /evo-discover/1.0.0/srd/subsystem` 中直接看到，但当前仓库里的 nginx 规则或已整理的业务入口里，没有全部出现稳定的外部 URL 前缀。  
也就是说，它们至少是“内部已注册服务”，但是否直接对外暴露、是否走 `/evo-apigw/<serviceName>/**`，还需要再结合业务代码或现场抓包确认。

| discover 中的服务名 | 注册地址 | 端口 | 当前判断 |
|---|---|---:|---|
| `evo-runs-adapt` | `192.168.131.46` | `8007` | 已注册；更像 `evo-runs` 侧的适配服务，未见稳定外部前缀 |
| `evo-mes` | `127.0.0.1` | `7086` | 已注册；与 `/vod/**`、`/hls/**` 所在回放流媒体端口一致 |
| `flv` | `127.0.0.1` | `7886` | 已注册；与 `/live/**` 所在流媒体端口一致 |
| `nacos-sync` | `192.168.131.46` | `8916` | 已注册；属于注册/同步相关服务，不属于常规业务网关入口 |
| `nacos:nacos-sync` | `192.168.131.46` | `8916` | NACOS 同步来源的注册名，不建议当作业务 URL 前缀使用 |
| `paas-Fnode` | `192.168.131.46` | `6556` | 已注册；当前文档中未找到对应 nginx 对外入口 |

## 1.2 `nacos-sync` 的特殊性

`nacos-sync` 和普通的 `evo-brm`、`evo-oauth` 这一类 Spring Boot 微服务不太一样。  
从 `evo-discover /srd/subsystem` 返回看，`nacos-sync` 的注册端口也是 `8916`，也就是和 `evo-discover` 本体共用同一个 Tomcat 入口。

实际链路可以理解为：

```text
/evo-apigw/nacos-sync/xxx.jsp
-> nginx 去掉 /evo-apigw/
-> evo-apigw 收到 /nacos-sync/xxx.jsp
-> 按服务名 nacos-sync 路由到 8916
-> 8916 上的 Tomcat 按 context path=/nacos-sync 处理
-> 最终落到 tomcat/srd/nacos-sync 对应的 webapp 目录
```

之所以会这样，是因为 `Evo-discover` 这里跑的不是“只有一个 `/evo-discover` 应用”的单应用模型，而是一个 Tomcat，多 context 共存。

关键依据如下：

- Tomcat `8916` 端口定义见 [server.xml](/D:/CodeAudit/大华ICC/opt/evo/evo-common/Evo-discover/tomcat/conf/server.xml#L69)
- `Host` 的 `appBase` 是 `srd`，见 [server.xml](/D:/CodeAudit/大华ICC/opt/evo/evo-common/Evo-discover/tomcat/conf/server.xml#L158)
- 部署脚本会把 `nacos-sync-server-0.4.8` 软链到 `tomcat/srd/nacos-sync`，见 [post_module_deploy.sh](/D:/CodeAudit/大华ICC/opt/evo/evo-common/Evo-discover/evo-discover/project-info/post_module_deploy.sh#L157)

对应部署动作是：

```sh
ln -sfn $module_deploy_path/nacos-sync-server-0.4.8 $module_deploy_path/$apache_name/srd/nacos-sync
```

所以这里的本质不是：

```text
nacos-sync -> 一个独立 8916 端口后的独立服务进程
```

而是：

```text
nacos-sync -> 8916 上 Tomcat 的一个 webapp context
```

因此，如果访问：

```text
/evo-apigw/nacos-sync/xxx.jsp
```

最终会命中：

```text
/opt/evo/evo-common/Evo-discover/tomcat/srd/nacos-sync/xxx.jsp
```

而这个目录本质上又是软链到：

```text
/opt/evo/evo-common/Evo-discover/nacos-sync-server-0.4.8
```

## 2. 不经过 `/evo-apigw/`，由 nginx 直接转发的路径

| 外部访问 URL 前缀 | 实际目标 | 端口 | 说明 |
|---|---|---:|---|
| `/evo-runs/**` | `evo-runs` | `8006` | 运维服务，nginx 直接转发，不经过 apigw |
| `/evo-runs/v1.0/ws` | `evo-runs` websocket | `8006` | 运维 websocket 长连接 |
| `/ras/unity/**` | `evo-dahua-open-api` | `9141` | nginx 直接转发到开放接口服务 |
| `/faceService/**` | `evo-dahua-open-api` | `9141` | nginx 直接转发到开放接口服务 |
| `/videoService/**` | `evo-dahua-open-api` | `9141` | nginx 直接转发到开放接口服务 |
| `/evo-websocket/**` | keepalive/websocket 后端 | 动态 | 走 nginx 特殊 websocket 转发 |
| `/evo-keepalive-http/**` | keepalive/http 后端 | 动态 | 走 nginx 特殊 keepalive 转发 |
| `/evo-oss/**` | `oss_backend` | `8927` | OSS 服务 |
| `/live/**` | 流媒体服务 | `7886` | flv 预览 |
| `/vod/**` | 流媒体服务 | `7086` | 回放 |
| `/hls/**` | 流媒体服务 | `7086` | hls 拉流 |

补充对应关系：

- `/live/**` 基本可对应 discover 中的 `flv:7886`
- `/vod/**`、`/hls/**` 基本可对应 discover 中的 `evo-mes:7086`
- `/evo-oss/**` 对应 discover 中的 `evo-oss:8927`，其 `extParams` 里还带有 `httpsPort=8928`

## 3. 会先 rewrite 到 `/evo-apigw/` 再进入网关的路径

这些路径在 nginx 中会先被改写，再交给 `evo-apigw`：

- `/SiteFineWeb/**`
- `/sfm/**`
- `/dos/**`
- `/emapService/**`
- `/ras/**`
- `/gdevice/**`

本质上对应的是：

```nginx
rewrite ^/(.*)$ /evo-apigw/$1 last;
```

因此：

```text
/emapService/xxx
-> nginx 改写成 /evo-apigw/emapService/xxx
-> 再交给 apigw
```

注意：

- 这类前缀不一定直接等于真实微服务名
- 很可能还会依赖 `rewriteRules`、服务别名或网关内部 rewrite 规则

## 4. 关键固定端口

| 服务 | 端口 |
|---|---:|
| `evo-apigw` | `8945` |
| `evo-discover` 注册中心 | `8916` |
| `evo-runs` | `8006` |
| `evo-runs-adapt` | `8007` |
| `evo-brm` | `8918` |
| `evo-oauth` | `8917` |
| `evo-event` | `8919` |
| `evo-emap` | `8920` |
| `evo-linkage` | `8922` |
| `evo-package` | `8923` |
| `evo-log` | `8924` |
| `evo-oss` | `8927` |
| `evo-cascade` | `8948` |
| `evo-dahua-open-api` | `9141` |
| `admin` | `9407` |
| `evo-job` | `8915` |
| `evo-doc` | `9139` |
| `evo-mr` | `9610` |
| `evo-arsm` | `8956` |
| `evo-mes` | `7086` |
| `flv` | `7886` |
| `nacos-sync` | `8916` |
| `paas-Fnode` | `6556` |
| `evo-oss httpsPort` | `8928` |

## 5. 最实用的判断规则

1. 如果 URL 是 `/evo-runs/...`，通常就是直接到 `8006` 的 `evo-runs`
2. 如果 URL 是 `/evo-apigw/服务名/...`，通常就是到对应 `system.name=服务名` 的微服务
3. 如果 URL 是 `/ras`、`/emapService`、`/sfm` 这类别名路径，需要结合 nginx rewrite 和 `evo-apigw` 的内部 rewrite 规则继续判断
4. 如果某服务已经出现在 `evo-discover /srd/subsystem` 返回里，但 nginx 里又存在专门的 `location` 直转规则，则优先按 nginx 直转理解，不要先入为主地认为一定经过 apigw
5. 如果 discover 返回的服务名本身对应的是 Tomcat 中的 webapp context，例如 `nacos-sync`，那么 apigw 转发到该“服务”后，后端不一定是独立进程接口，也可能是 Tomcat 下的 JSP/静态资源/Servlet 应用

## 6. 简要分析

### 6.1 外层 nginx 行为

- 外层 nginx 收到 `/evo-apigw/...`
- 通过 `proxy_pass https://hauc_backend/;` 转发到 `8945`
- 转发时会去掉 `/evo-apigw/` 前缀

示例：

```text
/evo-apigw/evo-brm/xxx
-> 8945 实际收到 /evo-brm/xxx
```

### 6.2 `evo-apigw` 内部行为

`evo-apigw.jar` 中存在以下关键类：

- `com.dahua.evo.apigw.config.CustomRouteLocator`
- `com.dahua.evo.apigw.config.CustomZuulConfig`
- `com.dahua.evo.apigw.handler.SubscribeCallBackHandler`

说明：

- `CustomRouteLocator` 负责生成和刷新 Zuul 路由
- 路由数据来自 discover 回调和定时轮询
- 路由规则主形态是 `/服务名/**`
- 支持版本兼容替换
- 支持 `rewriteRules` 下发的别名路由和参数化重写

### 6.3 版本处理

`CustomRouteLocator.getMatchingRoute()` 会根据：

- 请求路径中的版本
- 当前服务实际版本

做兼容判断，必要时替换为后端实际版本，再进行匹配和转发。

因此，某些请求即使 URI 中写的是旧版本号，只要版本兼容，也可能仍然被转发到新版本服务。

### 6.4 `8916` 上的 `nacos-sync` 为什么会落到文件目录

`8916` 对应的是 `Evo-discover` 自带的 Tomcat，而不是单一的 `evo-discover` Controller 应用。

在 [server.xml](/D:/CodeAudit/大华ICC/opt/evo/evo-common/Evo-discover/tomcat/conf/server.xml#L158) 里：

```xml
<Host name="localhost"  appBase="srd" unpackWARs="true" autoDeploy="true">
```

这表示 Tomcat 会把 `srd` 目录下的子目录当成各自的 webapp。

部署脚本又专门做了：

```sh
ln -sfn $module_deploy_path/nacos-sync-server-0.4.8 $module_deploy_path/$apache_name/srd/nacos-sync
```

所以：

```text
/nacos-sync/xxx.jsp
```

对 8916 上的 Tomcat 来说，就是：

```text
context path = /nacos-sync
资源目录 = tomcat/srd/nacos-sync
```

这也是为什么经由：

```text
/evo-apigw/nacos-sync/xxx.jsp
```

最后会访问到：

```text
/evo-common/Evo-discover/tomcat/srd/nacos-sync
```

下面这条链路在审计时可以直接套用：

```text
/evo-apigw/nacos-sync/xxx.jsp
-> nginx 去掉 /evo-apigw/
-> apigw 看到 /nacos-sync/xxx.jsp
-> 按 discover 注册信息把 nacos-sync 路由到 8916
-> 8916 Tomcat 按 webapp context /nacos-sync 处理
-> 命中 tomcat/srd/nacos-sync/xxx.jsp
```

## 7. 结论

可以把常规访问模式理解为：

```text
客户端请求 /evo-apigw/<serviceName>/...
-> nginx 去掉 /evo-apigw/
-> evo-apigw 收到 /<serviceName>/...
-> CustomRouteLocator 按服务名匹配路由
-> 转发到 discover 当前返回的该服务实例
```

对于最常见的审计和排查场景，优先用下面的方式判断：

```text
/evo-apigw/evo-brm/...            -> evo-brm
/evo-apigw/evo-oauth/...          -> evo-oauth
/evo-apigw/evo-event/...          -> evo-event
/evo-apigw/evo-emap/...           -> evo-emap
/evo-apigw/evo-cascade/...        -> evo-cascade
/evo-apigw/evo-dahua-open-api/... -> evo-dahua-open-api
/evo-apigw/evo-oss/...            -> evo-oss
/evo-runs/...                     -> evo-runs
/live/...                         -> flv:7886
/vod/...                          -> evo-mes:7086
/hls/...                          -> evo-mes:7086
/videoService/...                 -> evo-dahua-open-api
```

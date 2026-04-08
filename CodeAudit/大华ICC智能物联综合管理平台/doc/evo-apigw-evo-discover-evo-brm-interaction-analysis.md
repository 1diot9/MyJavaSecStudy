# evo-apigw、evo-discover、evo-brm 交互关系分析

## 1. 结论先说

这套体系里，三者的分工可以概括为：

- `evo-discover` 是注册中心和服务发现中心。
- `evo-brm` 是普通微服务提供者，启动后向 `evo-discover` 注册，并通过心跳续约维持在线状态。
- `evo-apigw` 是微服务网关，不直接用 Spring Cloud/Nacos 自动注册发现，而是通过大华自带的 discover 客户端向 `evo-discover` 订阅服务列表，然后把订阅结果转成 Zuul 路由。

它们之间最关键的直接交互是：

1. `evo-brm -> evo-discover`：注册、心跳、可选订阅。
2. `evo-apigw -> evo-discover`：注册自己、订阅全部服务、接收服务变更回调。
3. `evo-discover -> evo-apigw`：当服务列表变化时，把新的 `systemList` 回调给 `evo-apigw`。
4. `客户端 -> nginx -> evo-apigw -> evo-brm`：业务请求实际经过网关后，被路由到 `evo-brm`。

所以严格说，`evo-apigw` 不负责“注册中心”本身，而是消费 `evo-discover` 的注册结果，把它变成统一入口、鉴权边界、路由分发和一部分安全控制能力。

## 2. evo-apigw 是怎么起到“微服务网关/网安边界”作用的

### 2.1 它本质是 Zuul 网关，不是简单反向代理

`evo-apigw` 自身启在 `8945` 端口，配置里直接打开了 Zuul 相关参数，说明它是二层微服务网关，而不是单纯静态 nginx 转发。

关键依据：

- `evo/evo-common/evo-apigw/config/application.properties`
  - `server.port=${SERVICE.APIGW.SERVER.PORT}`
  - `SERVICE.APIGW.SERVER.PORT=8945`
  - 存在大量 `zuul.*` 配置
- `evo/evo-common/evo-apigw/evo-apigw.jar`
  - 存在 `com.dahua.evo.apigw.config.CustomRouteLocator`
  - 存在 `com.dahua.evo.apigw.config.CustomZuulConfig`

`CustomRouteLocator` 的实现表明：

- 路由不是写死的，而是运行期动态装载。
- 它维护 `currentZuulRoute`、`modulePathMap`、`subSystemList`。
- 它根据服务名生成 `/服务名/**` 这一类 Zuul 路由。
- 它还能处理 `rewriteRules`、版本兼容、以及同服务多节点的轮转。

也就是说，`evo-apigw` 的核心工作不是“固定把某路径代理到某端口”，而是“把 discover 下发的服务清单转换成可执行的网关路由表”。

### 2.2 它的服务发现来源不是 Nacos/Eureka，而是 discover 订阅结果

`evo-apigw` 配置里明确关闭了 `spring.cloud.nacos.discovery.enabled`，同时启用了 discover 组件配置：

- `register.config.location=classpath:config/component.properties`
- `register.server.name=evo-discover`
- `register.server.ip.list=127.0.0.1:8916`
- `register.subsystem.autoRegister=true`
- `register.subsystem.autoSubscribe=true`
- `register.subsystem.subscribeAll=true`

这说明它启动后会：

1. 把自己注册到 `evo-discover`
2. 再向 `evo-discover` 订阅全部服务
3. 用订阅结果生成路由

`SubscribeCallBackHandler` 反编译结果已经坐实这一点：

- 回调收到 `List<SubscribeSystemResult>`
- 拷贝成 `RegistSystem`
- 提取 `httpsPort`、`rewriteRules`
- 写入 Redis：
  - `subsystem:list`
  - `subsystem:route:list`
- 赋值给 `CustomRouteLocator.subSystemList`
- 发布 `RoutesRefreshedEvent`

所以 `evo-apigw` 的动态路由数据面，直接依赖 `evo-discover` 的订阅/回调机制。

### 2.3 它在“网关”上的作用，主要是统一入口、统一鉴权和统一流量控制

从配置和包内容看，`evo-apigw` 处在所有内部微服务前面，承担了典型微服务网关安全边界的职责。

#### 统一入口

外层 nginx 把 `/evo-apigw/` 打到 `8945`，然后 `evo-apigw` 再按内部路由分发到各微服务。

例如：

```text
/evo-apigw/evo-brm/user/list
-> nginx 去掉 /evo-apigw/
-> evo-apigw 收到 /evo-brm/user/list
-> CustomRouteLocator 匹配 evo-brm
-> 转发到 evo-brm 实例
```

这意味着内部服务不直接暴露给外部，而是先进入统一网关。

#### 统一鉴权

`evo-apigw/config/component.properties` 里有：

- `oauth.client.id=oauth_client`
- `oauth.client.secret=oauth_client`
- `oauth.authenticate=/`
- `oauth.permit=/**`

同时 `SubscribeCallBackHandler.serviceOnlineHandler()` 对 `evo-oauth` 有专门处理：

- `rushOauthServerUrl()`
- `OauthHelper.tokenByClient()`
- 把 `Authorization` 写入 `ServletContext`

这说明 `evo-apigw` 会感知 `evo-oauth` 的在线状态，并更新自身调用 OAuth 的能力。也就是说，认证中心与网关是直接联动的。

#### 统一路由控制

`CustomRouteLocator` 支持：

- 服务名路由
- 版本兼容替换
- `rewriteRules`
- 同服务多节点轮转
- `httpsPort` 扩展参数

这让网关成为“访问控制面”的统一入口。请求是否能到某服务，不只取决于 nginx，还取决于 discover 下发出来的服务和重写规则。

#### 统一限流与安全加固

从目录结构可见，`evo-apigw` 还带有：

- `config/rate-limiter.json`
- `lua/rate_limiter.lua`
- `rasp/rasp.jar`
- `rasp-engine.jar`

启动脚本 `bin/start.sh` 还会额外挂：

- `-javaagent:/opt/evo/evo-common/evo-apigw/rasp/rasp.jar`

这说明 `evo-apigw` 在部署层面还承担了：

- 接口限流
- Java RASP 运行时防护

所以如果把“微服务网关”理解成微服务统一安全边界，那么 `evo-apigw` 主要是靠“统一入口 + 鉴权前置 + 路由控制 + 限流 + RASP”来起作用。

## 3. evo-discover 是怎么起到注册中心和服务发现作用的

### 3.1 它暴露的就是标准注册/心跳/订阅/查询接口

`DiscoverController` 反编译结果已经明确了接口：

- `POST /srd/register`
- `POST /srd/heartbeat`
- `POST /srd/keep/heartbeat`
- `POST /srd/subscribe`
- `GET /srd/subsystem`
- `GET /srd/version/list`

控制器类级别映射是 `/srd`，所以这是整套 discover 的核心接口面。

对应含义：

- `register`：服务注册，返回 token 和 `expires=30`
- `heartbeat` / `keepHeartbeat`：拿 token 做续约
- `subscribe`：订阅服务变更
- `subsystem`：查询当前服务列表
- `version/list`：查询服务名到版本号的映射

### 3.2 它把注册信息存进缓存，并用 token 维持会话

`RegisterServiceImpl` 的逻辑很直接：

- 注册时生成 UUID token
- 用 token 作为 key 把 `DiscoverSystemDTO` 放进 `SystemListCache`
- 如果检测到相同 `magic` 的旧实例，会先淘汰旧实例

心跳时：

- 用 token 从 `SystemListCache` 取回实例
- 找不到就返回失败
- 找到则刷新缓存内容并续命

因此，`evo-discover` 的注册中心本质是：

- 服务实例信息缓存
- token 会话机制
- 周期性续约

这里不是 Spring Cloud 那种“应用名 + 实例元数据”透明注册模型，而是大华自己的 DTO/缓存协议。

### 3.3 它的订阅是“注册者给 token，订阅者给 monitor 回调地址”

`SubscribeDTO$Param` 字段已经可以确认：

- `token`
- `monitor`
- `subscribeAll`
- `subscribeCover`
- `systemList`

这意味着 discover 的订阅模型是：

- 订阅方必须先注册，拿到 token
- 然后提交一个订阅请求
- 订阅请求里带一个 `monitor` 回调地址
- 可选择：
  - 订阅全部服务
  - 只订阅部分服务
  - 覆盖订阅或增量订阅

`SubscribeServiceImpl` 也验证了这点：

- 先校验 token 是否仍在 `SystemListCache`
- `subscribeAll=true` 时，把监控地址记到 `SubscribeAllCache`
- 否则把监控地址和目标服务列表记到 `SubscribePartCache`
- 同时把 `token -> monitor` 记到 `TokenMagicCache`

所以 `evo-discover` 不是只提供“被动查询”，它还维护了一套“谁在订阅谁”的关系。

### 3.4 它通过定时通知把服务变化推送给订阅者

`DiscoverController.register()` 注册成功后会调用：

- `DiscoverHolder.switchOnNotice()`

`NoticeSchedule.discoverCheckSchedule()` 会：

1. 通过 Redis 锁 `discover:timer:lock:notice` 防止重复执行
2. 检查 `DiscoverHolder.needNotice()`
3. 如果需要，调用 `notifyService.noticeAllSubscriber()`

`NotifyServiceImpl.noticeAllSubscriber()` 负责真正回调：

- 对“订阅全部”的地址，直接 POST 一个 `NotifyDTO`
- 对“订阅部分”的地址，按订阅服务名和版本重新筛选后 POST 一个 `NotifyDTO`
- 如果回调失败，就把地址写入补偿缓存

`NotifyDTO` 结构里有：

- `operate`
- `date`
- `param.systemList`

这就说明 discover 的“服务发现”不仅能查，还能推：

- 查：`/srd/subsystem`
- 推：根据订阅关系向 `monitor` 回调 `systemList`

### 3.5 对 apigw 有一个特殊处理

`NotifyServiceImpl` 里有一个特殊常量：

- `:8945/discover/subscribe/callBack`

如果回调地址以这个后缀结尾，也就是 `evo-apigw` 的回调地址，discover 会给它发送更完整的一份服务列表，用于网关刷新路由。

这说明 discover 并不是“一视同仁地只负责注册”，而是对网关订阅者有明显的特殊适配。

## 4. evo-brm 等服务是怎么注册到 evo-discover 的

### 4.1 不是 Nacos/Eureka 自动注册，而是大华 discover 客户端自动完成

`evo-brm` 配置里明确写了：

- `spring.cloud.nacos.discovery.enabled=false`
- `register.config.location=classpath:config/component.properties`

而 `component.properties` 里有：

- `register.server.ip.list=${SERVICE.REGISTER.SERVER.LIST}`
- `register.server.version=1.0.0`
- `register.node.name=${system.name}`
- `register.node.port=${SERVICE.BRM.SERVER.PORT}`
- `register.subsystem.name=${system.name}`
- `register.subsystem.version=${system.service.version}`
- `register.subsystem.autoSubscribe=true`
- `register.subsystem.subscribeAll=true`

再结合依赖包：

- `dahua-client-discover-5.0.17-springboot2-RELEASE.jar`

可以判断：

- `evo-brm` 启动后，会由 discover 客户端组件自动读取这些 `register.*` 配置
- 自动向 `evo-discover` 发起注册
- 自动做心跳续约
- 也可能自动发起订阅

也就是说，`evo-brm` 本身并没有在业务代码里手写注册逻辑，注册动作主要由公共组件完成。

### 4.2 brm 的 discover 地址是部署脚本/配置转换出来的

`evo-brm/config/application-config.properties` 里：

- `SERVICE.REGISTER.SERVER.LIST=127.0.0.1:8916`

`evo-brm/scripts/user_defined.sh` 里又能看到：

- 它会把运行维护平台下发的
  - `SERVICE.REGISTER.SERVER.IP`
  - `SERVICE.REGISTER.SERVER.PORT`
- 合并成
  - `SERVICE.REGISTER.SERVER.LIST=ip:port,ip:port`

然后写回本地配置。

这说明 `evo-brm` 注册到哪台 discover，不是写死在代码里，而是部署时按环境下发。

### 4.3 brm 注册给 discover 的核心身份字段是什么

从配置和 discover 控制器可还原出 `brm` 注册上去的主要信息：

- 服务名：`evo-brm`
- 版本：`1.2.1`
- 地址：运行节点 IP
- 端口：`8918`
- 开发模式：`prod`
- 扩展参数：如果有，会放到 `extParams`

discover 收到注册后，会把：

- `subsystem` 信息
- `node.address`
- `developerModel`
- `extParams`

合并成一个 `DiscoverSystemDTO` 后保存。

### 4.4 brm 不一定只注册，也会参与订阅体系

`evo-brm` 配置里也打开了：

- `register.subsystem.autoSubscribe=true`
- `register.subsystem.subscribeAll=true`

这说明 `evo-brm` 不是只做“被调用者”，它也可能在启动后订阅其他服务的变化，用于自身依赖发现。

不过从三者主线来看，`brm` 最关键的角色仍然是：

- 作为业务服务提供者，把自己暴露给 `discover`
- 再由 `apigw` 通过订阅结果把它暴露为统一网关入口

## 5. 三者直接交互关系

### 5.1 注册阶段

```text
evo-brm 启动
-> discover 客户端读取 component.properties
-> POST /evo-discover/1.0.0/srd/register
-> evo-discover 生成 token
-> 把 brm 实例写入 SystemListCache
-> 返回 token, expires=30
```

`evo-apigw` 启动时也会走同样路径，把自己注册到 `evo-discover`。

### 5.2 心跳阶段

```text
evo-brm 持有 token
-> POST /evo-discover/1.0.0/srd/heartbeat
   或 /srd/keep/heartbeat
-> evo-discover 用 token 找到实例
-> 刷新缓存中的实例存活状态
```

如果 token 找不到，对应服务在 discover 看来就是掉线或会话失效。

### 5.3 订阅阶段

`evo-apigw` 会向 `evo-discover` 发起订阅，核心参数包括：

- `token`
- `monitor`
- `subscribeAll=true`

逻辑上等价于：

```text
evo-apigw
-> POST /evo-discover/1.0.0/srd/subscribe
-> 告诉 discover:
   我是合法已注册实例
   我的回调地址是 http://<apigw>:8945/discover/subscribe/callBack
   我要订阅全部服务
```

`evo-discover` 把这条订阅关系存入：

- `SubscribeAllCache`
- `TokenMagicCache`

### 5.4 服务变更通知阶段

当 `evo-brm` 新注册、重注册、掉线或服务列表变化时：

```text
evo-discover register/weedOut/变更
-> DiscoverHolder.switchOnNotice()
-> NoticeSchedule 定时检查
-> NotifyServiceImpl.noticeAllSubscriber()
-> POST 到 apigw 的 /discover/subscribe/callBack
-> body 中带 NotifyDTO.param.systemList
```

这一步是三者联动的关键：

- `brm` 的状态变化先进入 `discover`
- `discover` 再把变化推给 `apigw`
- `apigw` 用这个变化刷新路由

### 5.5 apigw 刷新路由阶段

`evo-apigw` 的 `SubscribeCallBackHandler.callBackRouteLoad()` 收到回调后会：

1. 把订阅结果转成 `RegistSystem`
2. 解析 `extParams` 中的 `httpsPort`、`rewriteRules`
3. 写 Redis：
   - `subsystem:list`
   - `subsystem:route:list`
4. 更新 `CustomRouteLocator.subSystemList`
5. 发布 `RoutesRefreshedEvent`

随后 `CustomRouteLocator` 会重新生成类似：

- `/evo-brm/** -> http://<brm-ip>:8918`

这样的 Zuul 路由。

### 5.6 业务请求阶段

最终对外业务链路是：

```text
客户端
-> nginx
-> evo-apigw:8945
-> CustomRouteLocator 匹配 evo-brm
-> 转发到 evo-brm:8918
-> evo-brm 返回结果
-> 结果回到客户端
```

所以三者的直接关系不是“客户端直接问 discover 找 brm”，而是：

- `discover` 负责维护“谁在线、地址是什么”
- `apigw` 负责把这个结果变成对外可访问的网关路由
- `brm` 负责提供真实业务能力

## 6. 可以把它理解成下面这张图

```text
                注册/心跳
evo-brm --------------------------> evo-discover
   ^                                   |
   |                                   |
   |                           订阅结果回调
   |                                   v
客户端 -> nginx -> evo-apigw -----> discover/subscribe/callBack
                     |
                     | 动态Zuul路由
                     v
                  evo-brm
```

再展开一点：

```text
1. brm 向 discover 注册自己
2. apigw 向 discover 订阅全部服务
3. discover 发现 brm 在线后，把 brm 放进 systemList 回调给 apigw
4. apigw 刷新出 /evo-brm/** 路由
5. 外部请求通过 apigw 被转发到 brm
```

## 7. 最终判断

### 7.1 evo-apigw 的作用

`evo-apigw` 的核心作用不是“保存注册信息”，而是：

- 接住外部统一入口
- 与 OAuth 联动形成鉴权边界
- 根据 `evo-discover` 的订阅结果生成动态路由
- 做版本兼容、重写、节点轮转
- 叠加限流与 RASP 防护

因此它承担的是“微服务入口层的网关/网安能力”。

### 7.2 evo-discover 的作用

`evo-discover` 通过：

- `/srd/register`
- `/srd/heartbeat`
- `/srd/subscribe`
- `/srd/subsystem`

构成了一套完整的注册中心和服务发现机制。

它既能保存在线服务，也能向订阅者主动推送服务列表变化。

### 7.3 evo-brm 的作用

`evo-brm` 通过 discover 客户端组件，按配置自动注册到 `evo-discover`，并依赖 token + 心跳维持在线。之后它会被 `evo-apigw` 订阅到，并暴露成统一网关入口下的一个微服务目标。

### 7.4 三者最本质的直接交互

最本质的一句话是：

`evo-brm` 把“我是谁、我在哪”报给 `evo-discover`，`evo-discover` 再把这份信息推给 `evo-apigw`，`evo-apigw` 最终把它变成外部请求真正能走到的网关路由。

## 8. 补充：evo-oauth 在其中起什么作用

### 8.1 先说结论

`evo-oauth` 的角色不是注册中心，也不是网关，而是独立的认证授权中心。

它负责的核心事情是：

- 给用户端、客户端颁发 token
- 提供 `check_token` / ticket 校验能力
- 维护 token、refresh token、ticket 等认证态
- 为各微服务提供统一的“认证真源”

但是，这套系统里“拿着 token 的请求到底在哪里被验”，并不是统一在 `evo-apigw` 完成，而更像是：

- `evo-oauth` 负责签发和校验依据
- `evo-brm` 这类微服务自己通过公共 OAuth 客户端组件去调用 `evo-oauth` 做 token 校验
- `evo-apigw` 只感知 `evo-oauth` 的存在，更多是为了路由联动、客户端 token 获取、以及部分网关侧调用，不是统一资源服务器入口

所以你的调试结论是对的：这套项目里，OAuth 认证过程的关键校验点更靠近每个微服务，而不是全部前置在 `apigw`。

### 8.2 evo-oauth 本身就是一个完整 OAuth2 授权服务

`evo-oauth` 的 jar 里直接存在：

- `AuthorizationServerConfiguration`
- `WebSecurityConfig`
- `MyRedisTokenStore`
- `MyDefaultTokenServices`
- `OauthController`
- `TicketController`

`AuthorizationServerConfiguration` 反编译结果表明：

- 它是 `AuthorizationServerConfigurerAdapter`
- 配了 `JdbcClientDetailsService`
- 配了 Redis `TokenStore`
- 提供 `ResourceServerTokenServices`
- 支持多种 `TokenGranter`
  - `ClientCredentialsTokenGranter`
  - `AuthorizationCodeTokenGranter`
  - `RefreshTokenGranter`
  - 自定义 password / sms / wechat / ticket 等

同时它的安全配置里明确有：

- `allowFormAuthenticationForClients()`
- `checkTokenAccess("permitAll()")`

这说明它对外提供的典型能力就是：

- `/oauth/token`
- `/oauth/check_token`
- 票据类校验接口

因此 `evo-oauth` 是标准意义上的“授权服务器 + token 校验中心”。

### 8.3 evo-oauth 也会注册到 evo-discover

`evo-oauth/config/component.properties` 里也有：

- `register.server.ip.list=127.0.0.1:8916`
- `register.subsystem.name=${system.name}`
- `register.subsystem.version=1.0.0`
- `register.subsystem.autoSubscribe=true`
- `register.subsystem.subscribeAll=true`

所以它和 `evo-brm` 一样，也是 discover 体系中的普通服务：

- 启动后注册到 `evo-discover`
- 也可能订阅其他服务
- 因此 `evo-apigw` 能通过 discover 发现它

这也解释了为什么 `SubscribeCallBackHandler` 里对 `evo-oauth` 有专门逻辑。

## 9. evo-oauth 与前面三者的关系

### 9.1 与 evo-discover 的关系

`evo-oauth` 和 `evo-brm` 一样，属于被 discover 管理的服务实例。

关系是：

```text
evo-oauth -> evo-discover
```

包括：

- 注册
- 心跳
- 被其他服务发现

所以 discover 不关心它是不是认证中心，只把它当作一个名为 `evo-oauth` 的微服务。

### 9.2 与 evo-apigw 的关系

这里要区分两层关系。

#### 第一层：服务发现关系

`evo-apigw` 会从 discover 订阅到 `evo-oauth` 的实例信息，把它也纳入路由表。

所以理论上存在这样的网关访问路径：

```text
/evo-apigw/evo-oauth/**
```

#### 第二层：网关自身对 oauth 的依赖关系

`SubscribeCallBackHandler.serviceOnlineHandler()` 里对 `evo-oauth` 有专门处理：

- `rushOauthServerUrl()`
- `OauthHelper.tokenByClient()`
- `OauthClient.setToken(...)`

这说明 `apigw` 在 `evo-oauth` 上线时，会主动刷新自己访问认证中心所需的客户端 token。

但要注意，这不等于：

- 所有业务请求先在 `apigw` 完成资源访问校验

因为 `apigw` 自己的配置里：

- `oauth.authenticate=/`
- `oauth.permit=/**`

这个组合实际非常像“网关层并未真正对业务路径执行强制 OAuth 拦截”。

所以 `apigw` 和 `oauth` 的关系更接近：

- `apigw` 认识 `oauth`
- `apigw` 需要能调用 `oauth`
- `apigw` 可以把请求路由到 `oauth`
- 但 `apigw` 不是整个系统唯一的 token 校验关口

### 9.3 与 evo-brm 的关系

`evo-brm` 对 `evo-oauth` 的依赖，比 `apigw` 更“实打实”。

`evo-brm/lib` 里直接有：

- `dahua-client-oauth-5.0.17-springboot2-RELEASE-0910.jar`

这个公共组件里有：

- `com.dahua.evo.client.oauth.oauth2.MyRemoteTokenServices`
- `com.dahua.evo.client.oauth.OauthClient`

而 `MyRemoteTokenServices` 反编译结果已经说明了关键逻辑：

- 它实现的是 `ResourceServerTokenServices`
- 内部有
  - `checkTokenEndpointUrl`
  - `clientId`
  - `clientSecret`
- `loadAuthentication(token)` 时会调用 `defaultOAuth2Authentication(token)`
- `defaultOAuth2Authentication(token)` 会走 `checkToken(token)`
- `checkToken(token)` 会向远端 OAuth 服务发 POST，请求 `checkTokenEndpointUrl`
- 请求头里带 `Basic clientId:clientSecret`

这说明 `evo-brm` 这一类微服务本地就具备“资源服务器”能力，只不过这能力不一定写在业务工程自己的类里，而是被封装在公共 jar 中。

也就是说：

```text
客户端带 Authorization 访问 evo-brm
-> evo-brm 本地的 OAuth 客户端/资源服务组件拦截
-> evo-brm 去请求 evo-oauth 的 check_token
-> evo-oauth 返回 token 对应的认证信息
-> evo-brm 再决定是否放行业务请求
```

这和你调试观察到的现象是一致的。

## 10. 为什么你会看到“oauth 过程发生在每个微服务里，而不是 apigw”

因为从当前代码和配置看，体系是“授权中心集中，资源校验分散”。

也就是：

- `evo-oauth` 集中签发和解释 token
- `evo-brm`、其他微服务各自作为资源服务器，在本地接入 `dahua-client-oauth`
- `apigw` 没有被配置成系统唯一的 OAuth 资源服务器入口

这套模式的典型表现就是：

1. 网关主要负责路由和入口统一
2. 每个微服务自己判断当前请求的 token 是否有效
3. token 真正的解释权仍然在 `evo-oauth`

所以更准确的话应该是：

- OAuth “真源”在 `evo-oauth`
- OAuth “资源访问校验动作”在各微服务
- `evo-apigw` 不是主要认证执行点

## 11. 把五者关系放在一起看

如果把 `evo-oauth` 也加进去，关系更接近下面这张图：

```text
evo-brm --------注册/心跳-------> evo-discover <-------注册/心跳-------- evo-oauth
   ^                                 |
   |                                 |
   |                           服务变更回调
   |                                 v
客户端 -> nginx -> evo-apigw ------------------------------+
                     |                                      |
                     | 动态路由                             | 发现 oauth / brm
                     v                                      |
                  evo-brm <-------- check_token -------- evo-oauth
```

拆开看：

- `discover` 负责“谁在线、谁在哪”
- `apigw` 负责“请求该路由到谁”
- `oauth` 负责“这个 token 是谁、能不能用”
- `brm` 负责“业务逻辑 + 本地资源访问校验”

## 12. 最终修正后的整体判断

前面三者的主线仍然成立，但如果把认证也纳入体系，最准确的描述应该是：

- `evo-discover`：注册中心、服务发现中心
- `evo-apigw`：统一入口、动态路由网关、部分安全边界
- `evo-oauth`：独立认证授权中心
- `evo-brm`：业务服务，同时本地接入 OAuth 资源访问校验

因此这套架构不是“API Gateway 完成统一 OAuth 鉴权”的典型前置网关模式，而更像是：

- 网关做流量入口与路由
- 微服务自己做 token 校验
- OAuth 中心提供统一签发与校验依据

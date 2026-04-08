# nginx网关转发配置梳理

## 1. 结论概览

这套 nginx 的网关转发配置，不是由单一文件完成，而是由“总入口 + 端口入口 + 路由 location + upstream 后端定义 + evo-apigw 增量配置”共同组成。

对网关转发真正起主导作用的文件，按生效链路可归纳为：

1. `3rdtool/nginx/conf/nginx.conf`
2. `3rdtool/nginx/conf/upstream.conf`
3. `3rdtool/nginx/conf/web_local_http_entry.conf`
4. `3rdtool/nginx/conf/web_local_https_entry.conf`
5. `3rdtool/nginx/conf/web_local_https_entry_ip6.conf`
6. `3rdtool/nginx/conf/location_http.conf`
7. `3rdtool/nginx/conf/location_https.conf`
8. `evo/evo-common/evo-apigw/nginx/api/evo_apigw.conf`
9. `evo/evo-common/evo-apigw/nginx/http/*.conf`
10. `evo/evo-common/evo-apigw/nginx/websocket/*.conf`
11. `evo/evo-common/evo-apigw/nginx/tcp/*.conf`

另外，下面这些文件也会间接影响网关转发行为：

1. `3rdtool/nginx/conf/web_public_http_entry.conf`
2. `3rdtool/nginx/conf/web_public_https_entry.conf`
3. `3rdtool/nginx/conf/web_local_http_entry2.conf`
4. `3rdtool/nginx/conf/web_local_runs.conf`
5. `3rdtool/nginx/conf/ext/*.conf`
6. `evo/evo-common/evo-runs/command/linux/security/config_nginx.sh`
7. `evo/evo-common/evo-runs/command/linux/security/config_debug_mode.sh`

## 2. 配置关联关系

主链路可以概括为：

```text
nginx.conf
  -> include upstream.conf
  -> include web_local_http_entry.conf
       -> include location_http.conf
       -> include ext/*.conf
  -> include web_local_https_entry.conf
       -> include location_https.conf
       -> include ext/*.conf
  -> include web_local_https_entry_ip6.conf
       -> include location_https.conf
       -> include ext/*.conf
  -> include /opt/evo/evo-common/evo-apigw/nginx/http/*.conf
  -> include /opt/evo/evo-common/evo-apigw/nginx/websocket/*.conf
  -> include /opt/evo/evo-common/evo-apigw/nginx/api/*.conf
  -> stream include /opt/evo/evo-common/evo-apigw/nginx/tcp/*.conf
```

可以把职责拆成 4 层：

1. `nginx.conf`
   负责总装配，决定哪些子配置被纳入最终运行配置。
2. `web_*_entry.conf`
   负责监听端口、协议、证书、公共请求头变量，然后把具体 URI 转发规则交给 `location_*.conf`。
3. `location_*.conf`
   负责“某个路径转发到哪个后端”，这是最核心的网关路由层。
4. `upstream.conf` 和 `evo-apigw/nginx/**/*.conf`
   负责定义后端目标地址或新增网关路由能力，属于“后端目标层”和“网关增量层”。

## 3. 主要配置文件作用

### 3.1 `3rdtool/nginx/conf/nginx.conf`

这是总入口文件，作用有三类：

1. 定义 nginx 全局运行参数。
2. 把各类子配置 `include` 进来。
3. 把 evo-apigw 生成或下发的路由配置并入 nginx。

与网关转发直接相关的关键点：

1. `include upstream.conf;`
   引入本地后端池定义。
2. `include web_local_http_entry.conf;`
   启用本地 HTTP 网关入口。
3. `include web_local_https_entry.conf;`
   启用本地 HTTPS 网关入口。
4. `include web_local_https_entry_ip6.conf;`
   启用 IPv6 HTTPS 入口。
5. `include /opt/evo/evo-common/evo-apigw/nginx/http/*.conf;`
   引入 HTTP 长连接类 upstream。
6. `include /opt/evo/evo-common/evo-apigw/nginx/websocket/*.conf;`
   引入 WebSocket 类 upstream。
7. `include /opt/evo/evo-common/evo-apigw/nginx/api/*.conf;`
   引入网关核心 API upstream，例如 `hauc_backend`。
8. `stream { include /opt/evo/evo-common/evo-apigw/nginx/tcp/*.conf; }`
   引入四层 TCP 代理规则。

需要注意的是：

1. `web_public_http_entry.conf`
2. `web_public_https_entry.conf`

在当前 `nginx.conf` 中默认是注释掉的，不会自动生效。它们通常由运维脚本按端口配置动态启用。

### 3.2 `3rdtool/nginx/conf/upstream.conf`

这是本地静态 upstream 定义文件，当前主要定义了：

1. `runs_backend -> 127.0.0.1:8006`
2. `oss_backend -> 127.0.0.1:8927`
3. `fsugw_backend -> 127.0.0.1:9088`

这些 upstream 会被 `location_https.conf`、`rs.conf` 等文件引用。也就是说：

1. `upstream.conf` 自己不决定路径；
2. 它只决定“名字对应的后端地址”；
3. 真正把 URI 绑定到 upstream 的，是 `location_*.conf` 或其他 `server/location` 文件。

### 3.3 `3rdtool/nginx/conf/web_local_http_entry.conf`

这是本地 HTTP 入口文件，主要作用：

1. 监听 `80` 端口。
2. 通过 `set_by_lua` 初始化 `$SerIP`、`$x_net_flag`、`$x_lc_mapping_net`。
3. `include location_http.conf;`
4. 如果未命中特殊条件，整体重定向到 HTTPS。

因此它更像“HTTP 入口壳子”，而不是完整的网关路由文件。HTTP 下真正的路径规则在 `location_http.conf`。

### 3.4 `3rdtool/nginx/conf/web_local_https_entry.conf`

这是本地 HTTPS 主入口文件，主要作用：

1. 监听 `443 ssl`。
2. 配置证书 `../ssl/service.pem` 和 `../ssl/service_unsercure.key`。
3. 初始化 `$SerIP`、`$x_net_flag`、`$x_lc_mapping_net`。
4. `include location_https.conf;`
5. 记录访问日志。

这是当前最核心的外部业务入口之一。绝大多数浏览器访问最终都会先进入这个 `server`，再由 `location_https.conf` 决定转发到哪里。

### 3.5 `3rdtool/nginx/conf/web_local_https_entry_ip6.conf`

作用与 `web_local_https_entry.conf` 类似，只是补充 IPv6 HTTPS 监听能力。它和 `web_local_https_entry.conf` 共用 `location_https.conf`，所以：

1. 路由规则本身没有复制一套；
2. 只是多了一个入口。

### 3.6 `3rdtool/nginx/conf/location_http.conf`

这个文件是 HTTP 场景下的路径规则文件，但当前内容相对少，主要是：

1. 访客相关 URI 直接转发到 `127.0.0.1:8910`。
2. 禁止访问 `/api/`、`Version.xml`、`/evo-apigw/health` 等敏感路径。

从现状看，HTTP 入口主要承担：

1. 少量兼容转发；
2. 拒绝部分敏感接口；
3. 其余请求再由 `web_local_http_entry.conf` 重定向到 HTTPS。

所以真正承担主网关转发职责的，还是 HTTPS 侧。

### 3.7 `3rdtool/nginx/conf/location_https.conf`

这是当前最关键的网关路由文件，负责把不同 URI 转发到不同后端。核心规则包括：

1. `/brm/ -> https://hauc_backend/`
2. `/evo-websocket -> http://$arg_keepalive_backend`
3. `/evo-keepalive-http -> http://$arg_http_keepalive_backend`
4. `/evo-pic/ -> http://$arg_oss_addr`
5. `/evo-image/... -> http://$arg_oss_addr/...`
6. `/evo-apigw/ -> https://hauc_backend/`
7. `/ras/unity|faceService|videoService/ -> http://127.0.0.1:9141`
8. `/SiteFineWeb|sfm|dos|emapService|ras|gdevice/ -> rewrite 到 /evo-apigw/...`
9. `/evo-runs/ -> http://runs_backend`
10. `/evo-oss/ -> http://oss_backend/`
11. `/live/ -> http://127.0.0.1:7886`
12. `/vod/ -> http://127.0.0.1:7086`
13. `/hls/ -> http://127.0.0.1:7086/`
14. `/ -> root /opt/evoWpms`

这个文件体现了两个关键模式：

1. 直接转发模式
   例如 `/evo-runs/`、`/evo-oss/`、`/live/`。
2. 统一网关汇聚模式
   例如 `/evo-apigw/`、`/brm/`，最终都走 `hauc_backend`。

所以，如果要排查“某个业务 URI 最终转到哪里”，第一优先级就是看 `location_https.conf`。

## 4. evo-apigw 相关配置的作用

### 4.1 `evo/evo-common/evo-apigw/nginx/api/evo_apigw.conf`

这个文件当前定义：

```conf
upstream hauc_backend {
    server 192.168.131.46:8945 weight=1 max_fails=2;
}
```

它是 `location_https.conf` 中以下关键规则的后端来源：

1. `/brm/`
2. `/evo-apigw/`

也就是说：

1. `location_https.conf` 决定“哪些 URI 进入统一 API 网关”；
2. `evo_apigw.conf` 决定“这个统一 API 网关实际在哪台机器、哪个端口”。

如果 `hauc_backend` 配置错，很多网关接口会整体失效。

### 4.2 `evo/evo-common/evo-apigw/nginx/http/*.conf`

当前可见文件是：

1. `keepalive_http.conf`

它定义了类似 `keepalive_brm_test` 的 upstream，供长连接 HTTP 场景使用。对应入口在 `location_https.conf` 的：

1. `/evo-keepalive-http`

### 4.3 `evo/evo-common/evo-apigw/nginx/websocket/*.conf`

当前可见文件包括：

1. `keepalive_event_websocket.conf`
2. `websocket_admin_backend.conf`

它们定义 WebSocket 后端 upstream，供以下路由使用或扩展使用：

1. `/evo-websocket`
2. 其他 websocket 类转发

### 4.4 `evo/evo-common/evo-apigw/nginx/tcp/*.conf`

当前可见文件是：

1. `keepalive_tcp.conf`

这部分不走 `http {}`，而是走 `stream {}`。作用是做 TCP 四层代理，不是普通 HTTP URI 路由，但仍属于网关转发体系的一部分。

## 5. 其他会影响网关转发的配置文件

### 5.1 `3rdtool/nginx/conf/web_public_http_entry.conf`

这是公网 HTTP 入口模板。当前 `listen 1`，明显是占位或待脚本替换值。它会：

1. 设置 `$x_net_flag=EXT_NET`
2. `include location_http.conf`
3. 未命中特例时重定向到 HTTPS

但因为 `nginx.conf` 里默认注释了 `include web_public_http_entry.conf;`，所以当前默认不生效。

### 5.2 `3rdtool/nginx/conf/web_public_https_entry.conf`

这是公网 HTTPS 入口模板。当前 `listen 2 ssl`，也是占位值。它会：

1. 设置 `$x_net_flag=EXT_NET`
2. `include location_https.conf`

与本地 HTTPS 入口相比，核心差异不是路由规则，而是公网场景下的入口属性。

### 5.3 `3rdtool/nginx/conf/web_local_http_entry2.conf`

这是一个额外的本地 HTTP 入口文件，当前会 `include location_https.conf`。它不是常驻入口，而是由调试模式脚本按需插入 `nginx.conf`，常见用途是：

1. 在调试场景下提供额外 HTTP 可访问入口；
2. 避免所有请求都被强制跳转到 HTTPS。

### 5.4 `3rdtool/nginx/conf/ext/*.conf`

`web_*_entry.conf` 都会 `include ext/*.conf`。这说明扩展模块可以直接往入口 `server` 中注入额外 `location` 或变量配置。

因此，排查某些特定业务的网关转发时，不能只看主文件，也要检查 `conf/ext/` 下是否新增了覆盖规则。

### 5.5 `3rdtool/nginx/conf/web_local_runs.conf`

这个文件是一个独立 `server`，主要监听运维相关入口，并 `include location_runs.conf`。它不属于主业务网关入口，但仍然是 nginx 对内暴露的一组转发入口。

## 6. 控制这些配置是否启用的脚本

### 6.1 `evo/evo-common/evo-runs/command/linux/security/config_nginx.sh`

这个脚本会动态修改 `nginx.conf` 和多个 `web_*_entry.conf`，包括：

1. 调整本地/公网 HTTP、HTTPS 监听端口。
2. 注释或取消注释 `web_public_http_entry.conf`。
3. 注释或取消注释 `web_public_https_entry.conf`。
4. 插入或删除 `web_local_http_entry2.conf`。

所以它虽然不是 nginx 配置文件本身，但它决定了哪些入口配置最终被纳入运行时生效集合。

### 6.2 `evo/evo-common/evo-runs/command/linux/security/config_debug_mode.sh`

这个脚本专门控制 `web_local_http_entry2.conf` 是否插入 `nginx.conf`，会影响是否额外开放调试 HTTP 入口。

## 7. 建议的排查顺序

如果后续需要分析某个请求为什么转发异常，建议按下面顺序检查：

1. 先看 `3rdtool/nginx/conf/nginx.conf`
   确认目标入口文件是否被 `include`。
2. 再看对应入口文件
   一般是 `web_local_https_entry.conf` 或 `web_public_https_entry.conf`。
3. 再看 `location_https.conf` 或 `location_http.conf`
   确认 URI 命中了哪个 `location`。
4. 再看 `upstream.conf` 或 `evo/evo-common/evo-apigw/nginx/api/*.conf`
   确认 upstream 名称对应的后端地址。
5. 最后检查 `conf/ext/*.conf`
   防止存在局部覆盖或新增规则。

## 8. 一句话总结

这套 nginx 网关配置的核心关系是：

`nginx.conf` 负责装配，`web_*_entry.conf` 负责监听入口，`location_https.conf`/`location_http.conf` 负责路径到后端的映射，`upstream.conf` 与 `evo-apigw/nginx/*.conf` 负责定义最终的后端目标。

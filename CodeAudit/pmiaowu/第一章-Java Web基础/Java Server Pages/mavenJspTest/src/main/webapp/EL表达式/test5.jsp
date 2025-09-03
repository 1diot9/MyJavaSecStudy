<%--
  Created by IntelliJ IDEA.
  User: snowstorm-maxy
  Date: 2025/9/1
  Time: 21:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>EL表达式测试-5</title>
</head>
<body>
<div>1.协议: ${pageContext.request.scheme}</div>
<div>2.服务器ip: ${pageContext.request.serverName}</div>
<div>3.服务器端口: ${pageContext.request.serverPort}</div>
<div>4.获取工程路径: ${pageContext.request.contextPath}</div>
<div>5.获取请求方法: ${pageContext.request.method}</div>
<div>6.获取客户端ip地址: ${pageContext.request.remoteHost}</div>
<div>7.获取会话的id编号: ${pageContext.session.id}</div>
</body>
</html>

<%--
  Created by IntelliJ IDEA.
  User: snowstorm-maxy
  Date: 2025/9/1
  Time: 21:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  HttpSession s = request.getSession();
  String sessionKey = "sk";
  String sessionValue = "test";
  s.setAttribute(sessionKey, sessionValue);
%>
<html>
<head>
  <title>EL表达式测试-4</title>
</head>
<body>
你好, ${sessionScope.get("sk")}
</body>
</html>

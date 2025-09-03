<%--
  Created by IntelliJ IDEA.
  User: snowstorm-maxy
  Date: 2025/8/31
  Time: 15:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>跳转前页面</title>
</head>
<body>
<%
    String username = "testname";
%>
<jsp:forward page="b.jsp">
    <jsp:param name="name" value="<%=username%>"/>
    <jsp:param name="other" value="test-test-test"/>
</jsp:forward>
</body>
</html>
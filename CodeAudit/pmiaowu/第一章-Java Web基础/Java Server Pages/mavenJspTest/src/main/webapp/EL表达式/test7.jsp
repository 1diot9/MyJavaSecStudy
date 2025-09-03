<%--
  Created by IntelliJ IDEA.
  User: snowstorm-maxy
  Date: 2025/9/1
  Time: 21:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    application.setAttribute("testName", "aa");
    session.setAttribute("testName", "bb");
    request.setAttribute("testName", "cc");
    pageContext.setAttribute("testName", "dd");
%>
<html>
<head>
    <title>EL表达式测试-7</title>
</head>
<body>
<div>applicationScope.testName的值: ${applicationScope.testName}</div>
<div>sessionScope.testName的值: ${sessionScope.testName}</div>
<div>requestScope.testName的值: ${requestScope.testName}</div>
<div>pageScope.testName的值: ${pageScope.testName}</div>
</body>
</html>

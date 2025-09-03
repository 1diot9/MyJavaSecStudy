<%--
  Created by IntelliJ IDEA.
  User: snowstorm-maxy
  Date: 2025/9/1
  Time: 21:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="EL表达式.Person" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  Person p = new Person("test", 18);
  pageContext.setAttribute("person", p);
%>
<html>
<head>
  <title>EL表达式测试-6</title>
</head>
<body>
<div>我的名称是: ${person["name"]}</div>
<div>我的年龄是: ${person.age}</div>
</body>
</html>

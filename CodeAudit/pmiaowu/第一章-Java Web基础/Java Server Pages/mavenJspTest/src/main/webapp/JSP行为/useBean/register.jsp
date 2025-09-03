<%--
  Created by IntelliJ IDEA.
  User: snowstorm-maxy
  Date: 2025/8/31
  Time: 15:16
  To change this template use File | Settings | File Templates.
--%>
// 文件地址: ./src/main/webapp/JSP行为/useBean/
// 文件名称: register.jsp

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%
    request.setCharacterEncoding("utf-8");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>注册成功</title>
</head>
<jsp:useBean id="user" scope="page" class="JSP行为.useBean.TestBean"/>
<jsp:setProperty name="user" property="*"/>
<jsp:setProperty property="userName" name="user" param="userName"/>
<jsp:setProperty property="password" name="user" param="password"/>
<jsp:setProperty property="age" name="user" param="age"/>
<body>
<div>注册成功:</div>
<div>使用Bean的属性方法</div>
<div>用户名: <%=user.getUserName()%></div>
<div>密 码: <%=user.getPassword()%></div>
<div>年 龄: <%=user.getAge()%></div>
<hr>
<div>使用getProperty</div>
<div>用户名: <jsp:getProperty name="user" property="userName"/></div>
<div>密 码: <jsp:getProperty name="user" property="password"/></div>
<div>年 龄: <jsp:getProperty name="user" property="age"/></div>
</body>
</html>

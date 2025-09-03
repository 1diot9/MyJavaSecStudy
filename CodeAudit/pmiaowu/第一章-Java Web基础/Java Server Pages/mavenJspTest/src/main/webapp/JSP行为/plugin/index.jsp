<%--
  Created by IntelliJ IDEA.
  User: snowstorm-maxy
  Date: 2025/8/31
  Time: 15:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html;charset=utf-8" %>
<html>
<head>
    <meta charset="utf-8">
    <title>jsp:plugin动作标签</title>
</head>
<body>

<h2>jsp:plugin动作标签</h2>

<jsp:plugin align="middle" type="applet" code="JSP行为.plugin.MyApplet" codebase="." width="200" height="200">
    <jsp:params>
        <jsp:param name="image" value="test.png"/>
    </jsp:params>
    <jsp:fallback>error happens when insert applet</jsp:fallback>
</jsp:plugin>

</body>
</html>





<%--// 打开: http://127.0.0.1:8081/mavenJspTest_war/JSP行为/plugin/index.jsp--%>

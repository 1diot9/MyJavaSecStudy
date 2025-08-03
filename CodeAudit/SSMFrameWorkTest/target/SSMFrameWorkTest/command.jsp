<%--
  Created by IntelliJ IDEA.
  User: likejun
  Date: 2019/11/1
  Time: 10:13 上午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>ExecCommand</title>
</head>
<body>
<form action="command/exec" method="POST">
    <input type="text" name="cmd">
    <input type="submit" value="执行ping测试">
    <br><br><br>
</form>
</body>
</html>
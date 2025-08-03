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
    <title>SelectBookByID</title>
</head>
<body>
<form action="book/queryBookById" method="post">
    <input type="text" name="ID">
    <input type="submit" value="根据ID查询书籍">
    <br><br><br>
</form>
</body>
</html>

<%--
  Created by IntelliJ IDEA.
  User: likejun
  Date: 2019/11/1
  Time: 9:56 上午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>${book.getBookName()}</title>
</head>
<body>
<table class="table table-hover table-striped">
    <thead>
    <tr>
        <th>书籍编号</th>
        <th>书籍名字</th>
        <th>书籍数量</th>
        <th>书籍详情</th>
        <th>操作</th>
    </tr>
    </thead>

    <tbody>
        <tr>
            <td>${book.getBookID()}</td>
            <td>${book.getBookName()}</td>
            <td>${book.getBookCounts()}</td>
            <td>${book.getDetail()}</td>
            <td>
                <a href="toUpdateBook?id=${book.getBookID()}">更改</a> |
                <a href="del/${book.getBookID()}">删除</a>
            </td>
        </tr>
    </tbody>
</table>
</body>
</html>

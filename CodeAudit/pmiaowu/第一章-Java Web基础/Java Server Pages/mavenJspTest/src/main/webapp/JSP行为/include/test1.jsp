<%--
  Created by IntelliJ IDEA.
  User: snowstorm-maxy
  Date: 2025/8/31
  Time: 10:52
  To change this template use File | Settings | File Templates.
--%>
// 文件地址: ./src/main/webapp/JSP行为/include/
// 文件名称: test1.jsp

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>
<jsp:include page="${param.value}" flush="true"></jsp:include>
<div>这是一个test1.jsp</div>

</body>





// 打开: http://127.0.0.1:8081/mavenJspTest_war/JSP行为/include/test1.jsp?value=head.jsp

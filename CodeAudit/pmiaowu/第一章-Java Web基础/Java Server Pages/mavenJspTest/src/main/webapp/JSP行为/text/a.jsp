<%--
  Created by IntelliJ IDEA.
  User: snowstorm-maxy
  Date: 2025/8/31
  Time: 15:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>jsp:text</title>
</head>
<body>
<jsp:text>${param.value}</jsp:text>

<jsp:text>模板数据</jsp:text>

<jsp:text>
    EL表达式测试2*2=${2*2}
</jsp:text>
</body>
</html>




<%--// 打开: http://127.0.0.1:8081/mavenJspTest_war/JSP行为/text/a.jsp?value=测试--%>

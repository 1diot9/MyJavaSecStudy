<%--
  Created by IntelliJ IDEA.
  User: snowstorm-maxy
  Date: 2025/8/31
  Time: 15:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>跳转后页面，同时接收参数</title>
</head>
<body>
<div> 参数一: <%=request.getParameter("name")%> </div>
<div> 参数二: <%=request.getParameter("other")%> </div>
</body>
</html>

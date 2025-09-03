<%--
  Created by IntelliJ IDEA.
  User: snowstorm-maxy
  Date: 2025/8/31
  Time: 15:30
  To change this template use File | Settings | File Templates.
--%>
<%--看网页源代码--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:element name="xmlElement1">
    <jsp:attribute name="xmlElementAttr1">${param.test1}</jsp:attribute>
    <jsp:body>jsp:body1</jsp:body>
</jsp:element>

<jsp:element name="xmlElement2">
    <jsp:attribute name="xmlElementAttr2"><%=request.getParameter("test2")%></jsp:attribute>
    <jsp:body>jsp:body2</jsp:body>
</jsp:element>

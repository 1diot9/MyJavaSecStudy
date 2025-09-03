<%--
  Created by IntelliJ IDEA.
  User: snowstorm-maxy
  Date: 2025/8/31
  Time: 10:29
  To change this template use File | Settings | File Templates.
--%>
// 文件名test2.jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%
    for (int i = 0; i < 3; i++) {
        out.println(i);
    }
%>

<%!
    int sum = 1;
    String name = "name";

    public String printName() {
        return name + "ooooo";
    }
%>

<% int sum = 8; %>
<% out.println(sum++); %>
<%= this.sum %>
<%= this.name %>
<%= this.printName() %>

-------------------xml语法-----------------------

<jsp:declaration>
    int a = 5;
</jsp:declaration>

<jsp:scriptlet>
    out.println(a);
</jsp:scriptlet>

<jsp:expression>
    (new java.util.Date()).toLocaleString()
</jsp:expression>

<jsp:scriptlet>
    out.println("Your IP address is " + request.getRemoteAddr());
</jsp:scriptlet>





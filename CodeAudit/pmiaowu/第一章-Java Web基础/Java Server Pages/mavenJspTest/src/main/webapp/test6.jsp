<%--
  Created by IntelliJ IDEA.
  User: snowstorm-maxy
  Date: 2025/8/30
  Time: 12:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.Listener.UserListener" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.getSession().setAttribute("currentUser", new UserListener());
    request.getSession().removeAttribute("currentUser");
%>

<%--
  Created by IntelliJ IDEA.
  User: snowstorm-maxy
  Date: 2025/8/30
  Time: 12:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.Listener.UserListener2" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.getSession().setAttribute("currentUser2", new UserListener2());
%>

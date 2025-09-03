<%--
  Created by IntelliJ IDEA.
  User: snowstorm-maxy
  Date: 2025/8/30
  Time: 12:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // 第一次调用setAttribute方法,触发创建操作
    request.setAttribute("attributeName3", "attributeValue-3-111111");

    // 第二次调用setAttribute方法,触发修改操作
    request.setAttribute("attributeName3", "attributeValue-3-222222");

    // 调用removeAttribute方法,触发销毁操作
    request.removeAttribute("attributeName3");
%>

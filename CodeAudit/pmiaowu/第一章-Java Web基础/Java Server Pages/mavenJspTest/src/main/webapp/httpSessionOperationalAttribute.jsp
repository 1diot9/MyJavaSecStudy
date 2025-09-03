<%--
  Created by IntelliJ IDEA.
  User: snowstorm-maxy
  Date: 2025/8/30
  Time: 12:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // 第一次调用setAttribute方法,触发创建操作
    request.getSession().setAttribute("attributeName2", "attributeValue-2-111111");

    // 第二次调用setAttribute方法,触发修改操作
    request.getSession().setAttribute("attributeName2", "attributeValue-2-222222");

    // 调用removeAttribute方法,触发销毁操作
    request.getSession().removeAttribute("attributeName2");
%>

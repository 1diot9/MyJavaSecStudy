<%--
  Created by IntelliJ IDEA.
  User: snowstorm-maxy
  Date: 2025/8/28
  Time: 23:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // 创建Session对象
    HttpSession session1 = request.getSession();

    // 输出SessionId
    out.println("session1: " + session1.getId());

    // 手动销毁session,杀掉会话
    session1.invalidate();
%>

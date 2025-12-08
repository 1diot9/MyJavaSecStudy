package tools;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;

public class ClassByteGen {
    public static byte[] getBytes(String code, String className) throws Exception {
//        String Abstract = "com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet";
        ClassPool pool = ClassPool.getDefault();
//        pool.importPackage(Abstract);
        pool.importPackage("java.io");
        pool.importPackage("java.nio.file");
        pool.importPackage("java.lang.reflect");
        pool.importPackage("java.nio.charset");
        pool.importPackage("java.util");
//        pool.insertClassPath(Abstract);
//        pool.insertClassPath("java.nio");
        CtClass ctClass = pool.makeClass(className);
//        ctClass.setSuperclass(pool.get(Abstract));
        CtConstructor ctConstructor = ctClass.makeClassInitializer();
        ctConstructor.setBody(code);
//        CtConstructor ctConstructor1 = new CtConstructor(new CtClass[]{}, ctClass);
//        ctConstructor1.setBody(code);
//        ctClass.addConstructor(ctConstructor1);
        ctClass.writeFile("ClassByteGen");
        return ctClass.toBytecode();
    }
}

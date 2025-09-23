package com.test;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;

public class VmArgsBypass {
    public static void main(String[] args) throws Exception {
        byte[] bytes = Files.readAllBytes(Paths.get("Calc.class"));
        Method method = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
        method.setAccessible(true);
        Class clazzLoader = (Class) method.invoke(ClassLoader.getSystemClassLoader(), bytes, 0, bytes.length);
        clazzLoader.newInstance();
    }
}

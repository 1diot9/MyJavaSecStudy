package com.test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Failed {
    public static void main(String[] args) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        byte[] bytes = Files.readAllBytes(Paths.get("Calc.class"));
        Method method = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
        method.setAccessible(true);
        Class clazzLoader = (Class) method.invoke(ClassLoader.getSystemClassLoader(), bytes, 0, bytes.length);
        clazzLoader.newInstance();
        // 抛出：Unable to make protected final java.lang.Class java.lang.ClassLoader.defineClass(java.lang.String,byte[],int,int)
        // throws java.lang.ClassFormatError accessible: module java.base does not "opens java.lang" to unnamed module @6f496d9f
    }
}

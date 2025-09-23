package com.test;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ModuleBypass {
    public static void main(String[] args) throws Exception {
        patchModule(ModuleBypass.class, ClassLoader.class);

        byte[] bytes = Files.readAllBytes(Paths.get("Calc.class"));
        Method method = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
        method.setAccessible(true);
        Class clazzLoader = (Class) method.invoke(ClassLoader.getSystemClassLoader(), bytes, 0, bytes.length);
        clazzLoader.newInstance();
    }

    public static void patchModule(Class current, Class target) throws Exception {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe) field.get(null);

        // 所有Class的数据结构都是一样的，相同字段的偏移量也是一样的
        long l = unsafe.objectFieldOffset(Class.class.getDeclaredField("module"));
        Module targetModule = target.getModule();
        unsafe.putObject(current, l, targetModule);
    }
}

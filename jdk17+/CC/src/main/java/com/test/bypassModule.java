package com.test;


import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class bypassModule {
    public static void patchModule(Class current, Class target) throws Exception {
        Unsafe unsafe = getUnsafe();
        long offset = unsafe.objectFieldOffset(Class.class.getDeclaredField("module"));
//        Method method = Class.class.getDeclaredMethod("getModule");
//        method.setAccessible(true);
//        Object targetModule = method.invoke(target);
        Module targetModule = target.getModule();
        unsafe.putObject(current, offset, targetModule);
    }

    public static void patch(Class clazz) throws Exception {
        Unsafe unsafe = getUnsafe();
        long offset = unsafe.objectFieldOffset(Class.class.getDeclaredField("module"));
        unsafe.putObject(clazz, offset, Object.class.getModule());
    }

    public static Unsafe getUnsafe() throws Exception{
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        Unsafe unsafe = (Unsafe) theUnsafe.get(null);
        return unsafe;
    }

    public static void getOffset(Field f) throws Exception {
        Unsafe unsafe = getUnsafe();
        long offset = unsafe.objectFieldOffset(f);
        System.out.println(offset);
    }

}

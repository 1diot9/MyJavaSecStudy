package com.test;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class nullModule {
    public static void main(String[] args) throws Exception {
        Field field = Class.class.getDeclaredField("module");
        bypassModule.getOffset(field);
        Unsafe unsafe = bypassModule.getUnsafe();
        unsafe.getAndSetObject(nullModule.class, 48, null);
        System.out.println(nullModule.class.getModule());
    }
}

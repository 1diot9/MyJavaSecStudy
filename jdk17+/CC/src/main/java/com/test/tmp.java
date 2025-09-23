package com.test;

import java.lang.reflect.Field;

public class tmp {
    public static void main(String[] args) {
        try {
            Field field = Module.class.getDeclaredField("name");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}

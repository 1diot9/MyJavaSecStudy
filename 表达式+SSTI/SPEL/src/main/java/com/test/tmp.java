package com.test;

import org.springframework.expression.Expression;

public class tmp {
    public static void main(String[] args) throws ClassNotFoundException {
        ClassLoader classLoader = Expression.class.getClassLoader();
        System.out.println(classLoader);
        Class<?> aClass = ClassLoader.getSystemClassLoader().loadClass("java.lang.Runtime");
    }
}

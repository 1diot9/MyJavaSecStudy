package com.example;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;

import java.lang.reflect.Method;

public class Test {
    public static void test(){
        System.out.println("test");
    }

    public static void main(String[] args) throws NoSuchMethodException {
        Method declaredMethod = TemplatesImpl.class.getDeclaredMethod("newTransformer", null);
        System.out.println(declaredMethod);
    }
}

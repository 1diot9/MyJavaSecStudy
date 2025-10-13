package com.test.study;

import com.test.pojo.Baka;

public class Ways2Class {
    public static void main(String[] args) throws ClassNotFoundException {
        Baka baka = new Baka();

        Class<? extends Baka> aClass = baka.getClass();

        Class<?> aClass1 = Class.forName("com.test.pojo.Baka");

        Class<Baka> aClass2 = Baka.class;

        System.out.println(aClass.equals(aClass1));
        System.out.println(aClass.equals(aClass2));
        System.out.println(aClass1.equals(aClass2));
    }
}

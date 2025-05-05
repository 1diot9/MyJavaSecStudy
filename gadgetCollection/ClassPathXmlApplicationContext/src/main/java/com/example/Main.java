package com.example;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String xml = "http://localhost:7778/1.xml";
        Class<?> aClass = Class.forName("org.springframework.context.support.ClassPathXmlApplicationContext");
        Constructor<?> constructor = aClass.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);
        constructor.newInstance(xml);


    }
}
package com.test.study;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Reflection2Runtime {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
        Class<?> aClass = Class.forName("java.lang.Runtime");
        Constructor<?> declaredConstructor = aClass.getDeclaredConstructor();
        declaredConstructor.setAccessible(true);
        Runtime runtime = (Runtime) declaredConstructor.newInstance();
        runtime.exec("calc");

        InputStream inputStream = Runtime.getRuntime().exec("whoami").getInputStream();
        System.out.println(IOUtils.toString(inputStream));
    }
}

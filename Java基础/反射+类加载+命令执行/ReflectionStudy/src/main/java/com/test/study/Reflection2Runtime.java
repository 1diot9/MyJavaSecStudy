package com.test.study;

import org.apache.commons.io.IOUtils;

import java.io.*;
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

        inputStream = Runtime.getRuntime().exec("whoami").getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        System.out.println(stringBuilder);



    }
}

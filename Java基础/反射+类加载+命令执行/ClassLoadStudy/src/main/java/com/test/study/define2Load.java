package com.test.study;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;

public class define2Load {
    public static void main(String[] args) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        ClassLoader classLoader = define2Load.class.getClassLoader();
        byte[] bytes = Files.readAllBytes(Paths.get("Student.class"));
        Class<? extends ClassLoader> clazz = ClassLoader.class;
//        Constructor<? extends ClassLoader> declaredConstructor = clazz.getDeclaredConstructor();
//        declaredConstructor.setAccessible(true);
//        ClassLoader classLoader = declaredConstructor.newInstance();
        Method defineClass = clazz.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
        defineClass.setAccessible(true);
        Class<?> student = (Class) defineClass.invoke(classLoader, bytes, 0, bytes.length);
//        Class.forName("com.test.pojo.Student");
        student.newInstance();
//        Class.forName("com.test.pojo.Student");
    }


}

package com.test.study;

import com.test.pojo.Baka;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionMethods {
    public static void main(String[] args) throws Exception {
        getField();
    }

    public static void getMethod() throws Exception {
        Baka baka = new Baka();

        Class<?> aClass = Class.forName("com.test.pojo.Baka");
        // 获取所有public方法，包括父类的
        Method[] methods = aClass.getMethods();
        for (Method method : methods) {
            System.out.println(method.getName());
        }

        System.out.println("================================");

        // 获取自己类的所有方法
        Method[] declaredMethods = aClass.getDeclaredMethods();
        for (Method method : declaredMethods) {
            System.out.println(method.getName());
        }

        Method method = aClass.getDeclaredMethod("kiss", String.class);
        // 临时设置私有方法可访问
        method.setAccessible(true);
        method.invoke(baka, "1diot9");

        Method method1 = aClass.getDeclaredMethod("kiss", String.class, String.class);
        method1.setAccessible(true);
        method1.invoke(baka, "baka", "1diot9");

        Method method2 = aClass.getMethod("sit", String.class);
        // static方法可以不写obj参数
        method2.invoke(null, "here");
    }

    public static void getConstructor() throws Exception {
        Baka baka = new Baka();
        Class<?> aClass = Class.forName("com.test.pojo.Baka");
        Constructor[] constructors = aClass.getConstructors();
        for (Constructor constructor : constructors) {
            System.out.println(constructor.getName());
        }


        Constructor<?> constructor = aClass.getDeclaredConstructor(String.class, int.class, int.class);
        constructor.setAccessible(true);
        constructor.newInstance("1diot9", 0, 1);

        Constructor<?> constructor1 = aClass.getConstructor();
        constructor1.setAccessible(true);
        constructor1.newInstance();
    }

    public static void getField() throws Exception {
        Baka baka = new Baka();
        Class<?> aClass = Class.forName("com.test.pojo.Baka");
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            System.out.println(field.getName());
        }

        baka.setName("1diot9");

        Field field = aClass.getDeclaredField("name");
        field.setAccessible(true);
        // 反射修改字段
        field.set(baka, "anything");
        Object o = field.get(baka);
        System.out.println(o);


    }
}

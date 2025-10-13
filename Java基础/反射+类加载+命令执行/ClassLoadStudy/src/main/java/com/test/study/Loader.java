package com.test.study;

import com.test.pojo.Baka;

public class Loader {
    public static void main(String[] args) throws Exception {
        Class<Baka> bakaClass = Baka.class;
        System.out.println("=================");
//        Class.forName("com.test.pojo.Baka");    // 触发static
//        System.out.println("==================");
//        Class.forName("com.test.pojo.Baka", false, ClassLoader.getSystemClassLoader()); // 不触发
//        System.out.println("=================");
//        Class.forName("com.test.pojo.Baka", true, ClassLoader.getSystemClassLoader()); // 触发static
//        System.out.println("==================");
//        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
//        classLoader.loadClass("com.test.pojo.Baka");   // 无事发生
//        System.out.println("==================");
        Baka baka = Baka.class.newInstance();   // 触发无参构造和static
    }
}

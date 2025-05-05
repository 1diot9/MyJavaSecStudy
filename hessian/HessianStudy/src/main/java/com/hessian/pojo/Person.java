package com.hessian.pojo;

import java.util.HashMap;
import java.util.Properties;

public class Person implements java.io.Serializable {
    private String name;
    private int age;
//    private HashMap map;

    public Person() {
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
//        this.map = map;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

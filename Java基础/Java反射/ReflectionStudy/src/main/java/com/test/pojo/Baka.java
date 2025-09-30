package com.test.pojo;

public class Baka {
    public String name;
    protected int gender;
    private int age;

    public Baka() {
        System.out.println("Baka⑨");
    }

    public Baka(String name, int gender, int age) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        System.out.println(name + " " + gender + " " + age);
    }

    public static void sit(String name) {
        System.out.println("sit " + name);
    }

    public void hello(String name) {
        System.out.println("hello " + name);
    }

    protected void hug(String name) {
        System.out.println("hug " + name);
    }

    private void kiss(String name) {
        System.out.println("kiss " + name);
    }

    private void kiss(String alias, String name) {
        System.out.println("kiss " + alias + "(" + name + ")");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

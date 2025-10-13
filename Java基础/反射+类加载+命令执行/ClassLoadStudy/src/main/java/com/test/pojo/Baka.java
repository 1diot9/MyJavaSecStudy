package com.test.pojo;

public class Baka {
    public String name;

    static {
        System.out.println("static block");
    }

    public Baka() {
        System.out.println("no-arg constructor");
    }

    public Baka(String name) {
        this.name = name;
        System.out.println("name constructor");
    }

}

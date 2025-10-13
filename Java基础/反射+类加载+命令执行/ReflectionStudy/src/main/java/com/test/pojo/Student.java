package com.test.pojo;

import java.io.IOException;

public class Student {
    private int id;

    public Student() {
        System.out.println("Student no-arg constructor");
        try {
            Runtime.getRuntime().exec("calc");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static {
        System.out.println("Student static block");
        try {
            Runtime.getRuntime().exec("notepad");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

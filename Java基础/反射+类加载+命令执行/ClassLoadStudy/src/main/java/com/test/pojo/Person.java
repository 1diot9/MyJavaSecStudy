package com.test.pojo;

import java.io.IOException;

public class Person {
    public Person() {
        try {
            System.out.println("Person non-args constructor");
            Runtime.getRuntime().exec("calc");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static {
        System.out.println("Person static");
    }
}

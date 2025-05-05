package com.example;

import java.io.Serializable;

public class ToStringClass implements Serializable {
    private String name;

    public ToStringClass() {
    }

    public String getName() {
        System.out.println("toStringClass");
        return name;
    }
}

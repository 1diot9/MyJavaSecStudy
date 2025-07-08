package com;

public class Test01 {
    public static void main(String[] args) {
        System.out.println(args.length);
        String property = System.getProperty("java.class.path");
        System.out.println(property);
    }
}

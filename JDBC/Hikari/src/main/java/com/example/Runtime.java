package com.example;

import java.io.IOException;

public class Runtime {
    public static void exec(String cmd) {
        try {
            java.lang.Runtime.getRuntime().exec(cmd);
            System.out.println("cmd executed successfully");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

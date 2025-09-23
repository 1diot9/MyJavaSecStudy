package com.test;


import java.nio.file.Files;
import java.nio.file.Paths;

public class CC5 {
    public static void main(String[] args) throws Exception {
        byte[] bytes = Files.readAllBytes(Paths.get("CalcAbs.class"));

    }
}

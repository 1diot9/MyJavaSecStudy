package com.test;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MethodHandlesDefine {
    public static void main(String[] args) throws IOException, IllegalAccessException {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        byte[] bytes = Files.readAllBytes(Paths.get("D:/1tmp/classes/com_test_Calc.class"));
        lookup.defineClass(bytes);
    }
}

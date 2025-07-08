package com.pocGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class Tools {
    public static void main(String[] args) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get("D:\\1tmp\\classes\\ysoserial\\Pwner31283416227400.class"));
        String s = Base64.getEncoder().encodeToString(bytes);
        System.out.println(s);
    }
}

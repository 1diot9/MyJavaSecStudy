package com.test;

import ch.qos.logback.core.util.FileUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UrlEncodeFile {
    public static void main(String[] args) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get("./ascii.jar"));
        String encode = URLEncoder.encode(new String(bytes), "utf-8");
        System.out.println(encode);
    }
}

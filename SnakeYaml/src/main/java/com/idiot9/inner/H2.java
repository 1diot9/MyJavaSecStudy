package com.idiot9.inner;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;

public class H2 {
    public static void main(String[] args) throws IOException {
        h2();
    }

    public static void h2() throws IOException {
        Yaml yaml = new Yaml();
//        byte[] bytes = Files.readAllBytes(Paths.get("./poc/h2.poc"));
        FileInputStream fis2 = new FileInputStream("./poc/inner/h2.poc");
        FileInputStream fis0 = new FileInputStream("./poc/inner/h2-2.0.202.poc");
        yaml.load(fis2);
    }
}

package com.idiot9.inner;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class C3p0 {
    public static void main(String[] args) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream("./poc/inner/c3p0.poc");
        new Yaml().load(fis);
    }
}

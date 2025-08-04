package com.idiot9.outer;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Detect {
    public static void main(String[] args) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream("./poc/outer/detect.yml");
        new Yaml().load(fis);
    }
}

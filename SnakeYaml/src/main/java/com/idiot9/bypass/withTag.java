package com.idiot9.bypass;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class withTag {
    public static void main(String[] args) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream("./poc/bypass/withTag.yml");
        new Yaml().load(fis);
    }
}

package com.idiot9.bypass;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class multiYml {
    public static void main(String[] args) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream("./poc/bypass/multiYml.yml");
        new Yaml().load(fis);
    }
}

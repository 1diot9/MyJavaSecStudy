package com.idiot9.inner;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class OnlyJdk {
    public byte[][] bytes = {"hello".getBytes()};

    public OnlyJdk() {
    }

    public static void main(String[] args) throws FileNotFoundException {
        byte[][] bytes = {"hello".getBytes()};
//        dump(new OnlyJdk());
        load();
    }


    public static void load() throws FileNotFoundException {
        Yaml yaml = new Yaml();
        FileInputStream fis = new FileInputStream("./poc/inner/OnlyJdk.poc");
        Object load = yaml.load(fis);
        System.out.println(load);
    }

    public static void dump(Object obj) {
        Yaml yaml = new Yaml();
        System.out.println(yaml.dump(obj));
    }
}

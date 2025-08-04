package com.idiot9.tmp;

import org.yaml.snakeyaml.Yaml;

public class Test {
    public static void main(String[] args) {
        String s = "!!com.idiot9.tmp.User {name: \"max\"}";
        new Yaml().load(s);
    }

}

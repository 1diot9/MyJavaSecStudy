package com.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ClassPathXml {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("http://127.0.0.1:7777/1.xml");
    }
}

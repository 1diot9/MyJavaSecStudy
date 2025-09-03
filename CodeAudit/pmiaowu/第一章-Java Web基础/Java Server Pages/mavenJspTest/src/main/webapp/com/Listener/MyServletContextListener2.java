// 第一步
// 创建ServletContextListener类,测试接口
// 路径: ./项目/src/main/webapp/com/Listener/MyServletContextListener2.java
package com.Listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class MyServletContextListener2 implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("MyServletContextListener2类的contextInitialized方法,执行了");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("MyServletContextListener2类的contextDestroyed方法,执行了");
    }
}
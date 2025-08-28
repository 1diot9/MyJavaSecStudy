// 第一步
// 创建ServletContextListener类,测试接口
// 路径: ./项目/src/main/webapp/com/Listener/MyServletContextListener.java
package com.Listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MyServletContextListener implements ServletContextListener {
    // 启动服务器创建应用程序上下文时执行
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 获取服务器启动的初始参数
        // web.xml文件中配置的
        String name = sce.getServletContext().getInitParameter("name");

        System.out.println("MyServletContextListener类的contextInitialized方法,执行了,name参数的值为:" + name);
    }

    // 关闭服务器销毁程序上下文时执行
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 获取服务器启动的初始参数
        // web.xml文件中配置的
        String name = sce.getServletContext().getInitParameter("name");

        System.out.println("MyServletContextListener类的contextDestroyed方法,执行了,name参数的值为:" + name);
    }
}
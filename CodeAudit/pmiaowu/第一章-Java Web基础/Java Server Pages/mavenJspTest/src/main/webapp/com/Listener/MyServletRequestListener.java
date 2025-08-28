// 第一步
// 创建ServletRequestListener类,测试接口
// 路径: ./项目/src/main/webapp/com/Listener/MyServletRequestListener.java
package com.Listener;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

public class MyServletRequestListener implements ServletRequestListener {
    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        System.out.println("MyServletRequestListener类的requestInitialized方法,执行了,nameTest参数的值为:" + sre.getServletRequest().getParameter("nameTest"));
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        System.out.println("MyServletRequestListener类的requestDestroyed方法,执行了,nameTest参数的值为:" + sre.getServletRequest().getParameter("nameTest"));
    }
}
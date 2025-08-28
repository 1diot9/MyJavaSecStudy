// 第一步
// 创建HttpSessionListener类,测试接口
// 路径: ./项目/src/main/webapp/com/Listener/MyHttpSessionListener.java
package com.Listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class MyHttpSessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println("HttpSessionListener类的sessionCreated方法,执行了,sessionId为:" + se.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("HttpSessionListener类的sessionDestroyed方法,执行了,sessionId为:" + se.getSession().getId());
    }
}
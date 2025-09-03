// 第一步
// 创建HttpSessionAttributeListener类,测试接口
// 路径: ./项目/src/main/webapp/com/Listener/UserListener2.java
package com.Listener;

import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionEvent;
import java.io.Serializable;

public class UserListener2 implements HttpSessionActivationListener, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public void sessionWillPassivate(HttpSessionEvent se) {
        System.out.println("UserListener2类的sessionWillPassivate方法,执行了,它钝化了,sessionId为:" + se.getSession().getId());
    }

    @Override
    public void sessionDidActivate(HttpSessionEvent se) {
        System.out.println("UserListener2类的sessionDidActivate方法,执行了,它活化了,sessionId为:" + se.getSession().getId());
    }
}
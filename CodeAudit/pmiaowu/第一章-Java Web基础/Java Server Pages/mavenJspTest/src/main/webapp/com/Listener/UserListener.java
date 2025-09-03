// 第一步
// 创建HttpSessionAttributeListener类,测试接口
// 路径: ./项目/src/main/webapp/com/Listener/UserListener.java
package com.Listener;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.io.Serializable;

public class UserListener implements HttpSessionBindingListener, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        System.out.println("UserListener类的valueBound方法,执行了,name=" + event.getName() + "绑定了");
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        System.out.println("UserListener类的valueUnbound方法,执行了,name=" + event.getName() + "解除绑定了");
    }
}
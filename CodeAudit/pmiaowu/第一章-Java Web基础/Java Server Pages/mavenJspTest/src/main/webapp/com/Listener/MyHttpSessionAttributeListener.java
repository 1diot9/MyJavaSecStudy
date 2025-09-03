// 第一步
// 创建HttpSessionAttributeListener类,测试接口
// 路径: ./项目/src/main/webapp/com/Listener/MyHttpSessionAttributeListener.java
package com.Listener;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionAttributeListener;

public class MyHttpSessionAttributeListener implements HttpSessionAttributeListener {
    @Override
    public void attributeAdded(HttpSessionBindingEvent se) {
        System.out.println("MyHttpSessionAttributeListener类的attributeAdded方法,执行了" +
                ",放到域中的name为:" + se.getName() +
                ",放到域中的value为:" + se.getValue()
        );
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent se) {
        System.out.println("MyHttpSessionAttributeListener类的attributeReplaced方法,执行了" +
                ",获得修改前的name为:" + se.getName() +
                ",获得修改前的value为:" + se.getValue()
        );
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent se) {
        System.out.println("MyHttpSessionAttributeListener类的attributeRemoved方法,执行了" +
                ",删除的域中的name为:" + se.getName() +
                ",删除的域中的value为:" + se.getValue()
        );
    }
}
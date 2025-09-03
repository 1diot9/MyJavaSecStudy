// 第一步
// 创建ServletContextAttributeListener类,测试接口
// 路径: ./项目/src/main/webapp/com/Listener/MyServletContextAttributeListener.java
package com.Listener;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;

public class MyServletContextAttributeListener implements ServletContextAttributeListener {
    @Override
    public void attributeAdded(ServletContextAttributeEvent scae) {
        System.out.println("MyServletContextAttributeListener类的attributeAdded方法,执行了" +
                ",放到域中的name为:" + scae.getName() +
                ",放到域中的value为:" + scae.getValue()
        );
    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent scae) {
        System.out.println("MyServletContextAttributeListener类的attributeReplaced方法,执行了" +
                ",获得修改前的name为:" + scae.getName() +
                ",获得修改前的value为:" + scae.getValue()
        );
    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent scae) {
        System.out.println("MyServletContextAttributeListener类的attributeRemoved方法,执行了" +
                ",删除的域中的name为:" + scae.getName() +
                ",删除的域中的value为:" + scae.getValue()
        );
    }
}
// 第一步
// 创建HttpSessionAttributeListener类,测试接口
// 路径: ./项目/src/main/webapp/com/Listener/MyServletRequestAttributeListener.java
package com.Listener;

import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;

public class MyServletRequestAttributeListener implements ServletRequestAttributeListener {
    @Override
    public void attributeAdded(ServletRequestAttributeEvent srae) {
        System.out.println("MyServletRequestAttributeListener类的attributeAdded方法,执行了" +
                ",放到域中的name为:" + srae.getName() +
                ",放到域中的value为:" + srae.getValue()
        );
    }

    @Override
    public void attributeReplaced(ServletRequestAttributeEvent srae) {
        System.out.println("MyServletRequestAttributeListener类的attributeReplaced方法,执行了" +
                ",获得修改前的name为:" + srae.getName() +
                ",获得修改前的value为:" + srae.getValue()
        );
    }

    @Override
    public void attributeRemoved(ServletRequestAttributeEvent srae) {
        System.out.println("MyServletRequestAttributeListener类的attributeRemoved方法,执行了" +
                ",删除的域中的name为:" + srae.getName() +
                ",删除的域中的value为:" + srae.getValue()
        );
    }
}
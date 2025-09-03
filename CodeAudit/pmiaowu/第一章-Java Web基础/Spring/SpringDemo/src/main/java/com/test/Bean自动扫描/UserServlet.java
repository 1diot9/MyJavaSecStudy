// 第三步
// 目录: ./SpringDemo/src/main/java/com/test/Bean自动扫描/
// 文件名: UserServlet.java
package com.test.Bean自动扫描;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * 控制层
 * 注: 如果不指定名称, 则默认就是类名的首字母的小写,也就是userServlet
 */
@Controller("userServlet")
/**
 * 设置bean的作用域范围为prototype
 */
@Scope("prototype")
public class UserServlet {
    @Resource
    private IUserDao dao;

    public void doGet() {
        dao.addUser();
        dao.delUser();
        dao.updateUser();
        dao.queryUser();
    }
}
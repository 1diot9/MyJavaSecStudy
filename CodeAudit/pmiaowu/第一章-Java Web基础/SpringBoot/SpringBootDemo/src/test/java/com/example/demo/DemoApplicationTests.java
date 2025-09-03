package com.example.demo;

import com.example.demo.bean.UserBean;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    UserService userService;

    @Test
    public void contextLoads() {
        UserBean userBean = userService.loginIn("admin","admin123");
        if (userBean != null) {
            System.out.println("该用户输入账号为: " + userBean.getUsername());
            System.out.println("该用户输入密码为: " + userBean.getPassword());
            System.out.println("返回的用户ID为: " + userBean.getId());
        } else {
            System.out.println("账号或是密码错误!");
            System.out.println("查询不到该用户");
        }
    }

}
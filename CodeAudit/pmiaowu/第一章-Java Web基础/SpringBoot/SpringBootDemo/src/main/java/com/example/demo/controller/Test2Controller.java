package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Test2Controller {
    // 获取某个配置文件的值-测试
    @Value("${spring.datasource.username}")
    private String dbusername;

    // 方法直接返回字符串-测试
    @RequestMapping("/test2")
    @ResponseBody
    public String test() {
        return dbusername;
    }
}

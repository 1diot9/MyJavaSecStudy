package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Test3Controller {
    // 页面赋值-测试
    @RequestMapping("/test3")
    public String test(Model model) {
        model.addAttribute("name", "小明");
        model.addAttribute("age", 24);
        model.addAttribute("info", "我是一个爱打机的青年");
        return "test3";
    }
}

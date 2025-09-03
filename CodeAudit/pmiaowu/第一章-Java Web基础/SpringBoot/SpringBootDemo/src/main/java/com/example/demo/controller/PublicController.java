package com.example.demo.controller;

import com.example.demo.bean.UserBean;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PublicController {

    //将Service注入Web层
    @Autowired
    UserService userService;

    @RequestMapping("/public/login")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/password/login", method = RequestMethod.POST)
    @ResponseBody
    public String passwordLogin(String username, String password) {
        UserBean userBean = userService.loginIn(username, password);
        if (userBean != null) {
            return "login_success";
        } else {
            return "login_error";
        }
    }
}
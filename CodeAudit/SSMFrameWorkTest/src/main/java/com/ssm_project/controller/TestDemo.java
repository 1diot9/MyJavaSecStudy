package com.ssm_project.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TestDemo {

    @RequestMapping(value = "/Test",method = RequestMethod.GET)
    public String TestFunction(){
        return "Success";
    }
}

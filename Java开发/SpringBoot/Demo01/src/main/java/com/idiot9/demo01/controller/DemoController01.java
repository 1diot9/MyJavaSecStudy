package com.idiot9.demo01.controller;

import com.idiot9.demo01.entity.Student;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DemoController01 {


    @GetMapping("/")
    @ResponseBody
    public String index(){
        return "hello world";
    }

    @GetMapping("/student")
    @ResponseBody
    public Student student(){
        Student student = new Student();
        student.setId(11);
        student.setName("1diot9");
        student.setAge(22);
        return student;
    }
}

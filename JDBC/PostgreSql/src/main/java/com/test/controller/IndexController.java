package com.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.DriverManager;

@Controller
public class IndexController {

    @ResponseBody
    @RequestMapping("/jdbc")
    public String jdbc(String url) {
        try {
            DriverManager.getConnection(url);

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            return sw.toString();
        }
        return "done.";
    }

}

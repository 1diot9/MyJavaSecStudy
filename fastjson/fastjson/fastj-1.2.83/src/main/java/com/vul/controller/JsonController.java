package com.vul.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class JsonController {
    @RequestMapping("/json")
    @ResponseBody
    public String json(@RequestBody String jsonStr) {
        Object parse = JSON.parse(jsonStr);
        return "success";
    }
}

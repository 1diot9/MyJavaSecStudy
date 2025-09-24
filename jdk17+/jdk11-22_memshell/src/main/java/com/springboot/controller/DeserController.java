package com.springboot.controller;

import org.springframework.cglib.core.ReflectUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.expression.Expression;

import java.util.Base64;

// 内存马用jmg生成，springboot3要选tomcat+Jakata
@Controller
public class DeserController {
    @RequestMapping("/deser")
    public void deser(@RequestParam("data") String data) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(data);
        ReflectUtils.defineClass("org.springframework.expression.Test", bytes, Expression.class.getClassLoader(), null, Expression.class).newInstance();
    }
}

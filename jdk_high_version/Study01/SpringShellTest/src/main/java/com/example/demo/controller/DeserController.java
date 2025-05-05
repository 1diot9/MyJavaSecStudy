package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;

@Controller
public class DeserController {
    @RequestMapping("/deser")
    @ResponseBody
    public String deser(@RequestParam("data") String data) throws IOException, ClassNotFoundException {
        byte[] decode = Base64.getDecoder().decode(data);
        ByteArrayInputStream bais = new ByteArrayInputStream(decode);
        ObjectInputStream ois = new ObjectInputStream(bais);
        ois.readObject();
        ois.close();
        return "deser";
    }

    @RequestMapping("/test")
    @ResponseBody
    public String test(){
        return "test";
    }
}

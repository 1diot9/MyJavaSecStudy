package com.springboot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;

@Controller
public class SerController {
    @ResponseBody
    @RequestMapping("/ser")
    public String ser(@RequestParam String ser) throws IOException, ClassNotFoundException {
        byte[] decode = Base64.getDecoder().decode(ser);
        ByteArrayInputStream bais = new ByteArrayInputStream(decode);
        ObjectInputStream ois = new ObjectInputStream(bais);
        ois.readObject();
        ois.close();
        return ois.toString();
    }

    @ResponseBody
    @RequestMapping("/lookup")
    public String lookup(@RequestParam String url) throws NamingException {
        InitialContext initialContext = new InitialContext();
        initialContext.lookup(url);
        return url;
    }
}

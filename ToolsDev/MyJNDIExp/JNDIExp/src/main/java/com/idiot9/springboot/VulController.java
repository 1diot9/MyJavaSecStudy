package com.idiot9.springboot;

import com.beust.jcommander.Parameter;
import org.springframework.stereotype.Controller;
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
public class VulController {
    @ResponseBody
    @RequestMapping("/deser")
    public String deser(@RequestParam String base64) throws IOException, ClassNotFoundException {
        byte[] decode = Base64.getDecoder().decode(base64);
        ByteArrayInputStream bais = new ByteArrayInputStream(decode);
        ObjectInputStream ois = new ObjectInputStream(bais);
        ois.readObject();
        ois.close();
        return ois.toString();
    }

    @ResponseBody
    @RequestMapping("/lookup")
    public String lookup(@RequestParam String url) throws IOException, ClassNotFoundException, NamingException {
        InitialContext initialContext = new InitialContext();
        initialContext.lookup(url);
        return initialContext.toString();
    }
}

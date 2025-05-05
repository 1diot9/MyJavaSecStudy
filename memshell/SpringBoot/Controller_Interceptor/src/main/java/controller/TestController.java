package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;

@Controller
public class TestController {
    @RequestMapping("/test")
    @ResponseBody
    public String test() {
        return "/test";
    }

    @RequestMapping("/deser")
    @ResponseBody
    public String deser(HttpServletRequest request, HttpServletResponse response) throws IOException, ClassNotFoundException {
        String data = request.getParameter("data");
        if (data != null) {
            byte[] bytes = Base64.getDecoder().decode(data);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            ois.readObject();
        }
        return "/deser";
    }
}

package test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ReturnViewTestController {
    @RequestMapping("/returnViewTest")
    public String returnViewTest() {
        return "/returnStringTestView";
    }
}
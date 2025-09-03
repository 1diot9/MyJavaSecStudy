package test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ReturnModelTestController {
    @RequestMapping("/returnModelTest")
    public String returnModelTest(Model model) {
        model.addAttribute("msg", "这是一个测试Model视图传递数据的测试界面");
        return "/returnModelTestView";
    }
}
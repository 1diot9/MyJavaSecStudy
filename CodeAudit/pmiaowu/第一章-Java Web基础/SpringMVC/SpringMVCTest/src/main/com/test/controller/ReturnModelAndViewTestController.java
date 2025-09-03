package test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReturnModelAndViewTestController {
    @RequestMapping("/returnModelAndViewTest")
    public ModelAndView returnModelAndViewTest() {
        ModelAndView mv = new ModelAndView();
        mv.addObject("message", "你好啊,这是一个传递数据的测试视图");
        mv.setViewName("/returnModelAndViewTestView");
        return mv;
    }
}
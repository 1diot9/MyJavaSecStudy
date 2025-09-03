// 创建个测试使用的控制器
// 目录: ./SpringMVCTest/src/main/com/test/controller/TestController.java
// 文件名: TestController.java
package test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestController {
    @RequestMapping("/hello")
    public ModelAndView sayHello(String name) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("msg", "你好啊," + name + ",这是一个欢迎界面");
        // 注:
        // 因为springmvc.xml设置了<property name="suffix" value=".jsp"></property>
        // 指定了请求文件的后缀是.jsp
        // 所以直接写成/hello就可以了
        // 如果没有在springmvc.xml设置后缀,那就需要写成/hello.jsp
        mv.setViewName("/hello");
        return mv;
    }
}
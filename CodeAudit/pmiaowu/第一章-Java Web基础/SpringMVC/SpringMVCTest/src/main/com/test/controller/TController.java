// 创建一个测试用的控制器
// 路径: ./项目/src/main/com/test/controller/TController.java
package test.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TController {
    @RequestMapping(value = "/tTest")
    public String tTest() {
        return "helloTest";
    }

    @RequestMapping(value = "/gotoTest")
    public String gotoTest() {
        return "gotoTest";
    }
}
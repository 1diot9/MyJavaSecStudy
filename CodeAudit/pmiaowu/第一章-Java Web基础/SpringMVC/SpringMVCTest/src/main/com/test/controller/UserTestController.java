// 路径: ./项目/src/main/com/test/controller/UserTestController.java
package test.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class UserTestController {
    @RequestMapping(value = "/userTest/{id}", method = RequestMethod.GET)
    public String testRestGet(@PathVariable Integer id) {
        return "test rest GET:" + id;
    }

    @RequestMapping(value = "/userTest/{id}", method = RequestMethod.POST)
    public String testRestPost(@PathVariable Integer id) {
        return "test rest POST:" + id;
    }

    @RequestMapping(value = "/userTest/{id}", method = RequestMethod.DELETE)
    public String testRestDelete(@PathVariable Integer id) {
        return "test rest DELETE:" + id;
    }

    @RequestMapping(value = "/userTest/{id}", method = RequestMethod.PUT)
    public String testRestPUt(@PathVariable Integer id) {
        return "test rest PUT:" + id;
    }
}





//访问方式-GET请求-1: http://xxx.com/userTest/1
//返回: test rest GET:1
//
//访问方式-POST请求-2: http://xxx.com/userTest/1
//返回: test rest POST:1
//
//访问方式-DELETE请求-3: http://xxx.com/userTest/1
//返回: test rest PUT:1
//
//访问方式-PUT请求-4: http://xxx.com/userTest/1
//返回: test rest DELETE:1
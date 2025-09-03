// 第一步
// 在./SpringDemo/src/main/java/com/test/创建个HelloWorld2.java
// 目录: ./SpringDemo/src/main/java/com/test/
// 文件名: HelloWorld2.java
package com.test;

import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Scope;

// 注册bean
@Component("helloWorldTest2")
// 设置bean的作用域范围为prototype
@Scope("prototype")
public class HelloWorld2 {
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public void getMessage() {
        System.out.println("Your Message: " + message);
    }
}
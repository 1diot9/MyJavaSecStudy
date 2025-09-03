// 第三步
// 目录: ./SpringDemo/src/main/java/com/test/基于注解的配置/注入数据的注解标签/Resource/
// 文件名: Dog.java
package com.test.基于注解的配置.注入数据的注解标签.Resource;

import org.springframework.stereotype.Component;

@Component("dog")
public class Dog {
    public String shout() {
        return "汪汪汪";
    }
}
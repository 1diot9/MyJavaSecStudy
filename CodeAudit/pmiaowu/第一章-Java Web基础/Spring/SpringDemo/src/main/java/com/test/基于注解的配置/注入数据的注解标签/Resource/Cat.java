// 第二步
// 目录: ./SpringDemo/src/main/java/com/test/基于注解的配置/注入数据的注解标签/Resource/
// 文件名: Cat.java
package com.test.基于注解的配置.注入数据的注解标签.Resource;

import org.springframework.stereotype.Component;

@Component("cat")
public class Cat {
    public String shout() {
        return "喵喵喵";
    }
}
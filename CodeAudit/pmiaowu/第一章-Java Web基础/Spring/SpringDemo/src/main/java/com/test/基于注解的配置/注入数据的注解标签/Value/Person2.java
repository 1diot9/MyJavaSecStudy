// 第二步
// 目录: ./SpringDemo/src/main/java/com/test/基于注解的配置/注入数据的注解标签/Value/
// 文件名: Person2.java
package com.test.基于注解的配置.注入数据的注解标签.Value;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("person2Test")
public class Person2 {
//    @Value(value="张三")/@Value("张三")对标XML配置<property name="name" value="张三"/>
    @Value("李四")
    private String name;
    @Value("18")
    private int age;

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }
}
// 第二步
// 通过注解创建一个Bean
// 目录: ./SpringDemo/src/main/java/com/test/基于注解的配置/创建对象的注解标签/
// 文件名: Person.java
package com.test.基于注解的配置.创建对象的注解标签;

import org.springframework.stereotype.Component;

// @Component // 直接省略value属性,默认名称为类名首字母小写,也就是Person转成person
// @Component("personTest")	// 省略value的写法
@Component(value = "personTest") // 全写
public class Person {
    private String name = "小王";

    public String getName() {
        return this.name;
    }
}
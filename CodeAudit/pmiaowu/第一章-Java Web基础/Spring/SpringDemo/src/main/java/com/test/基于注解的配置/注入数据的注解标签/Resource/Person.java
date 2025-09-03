// 第四步
// 目录: ./SpringDemo/src/main/java/com/test/基于注解的配置/注入数据的注解标签/Resource/
// 文件名: Person.java
package com.test.基于注解的配置.注入数据的注解标签.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("resourceRersonTest")
public class Person {
    @Value("王五")
    private String name;

//    @Resource(name = "beanId")对标XML配置<bean id="x" class="x" autowire="byName"></bean>
    @Resource(name = "cat")
    private Cat cat;
    @Resource(name = "dog")
    private Dog dog;

    public String speak() {
        return "我叫" + this.name + "," +
                "有只猫会" + this.cat.shout() + "叫," +
                "还有只狗会" + this.dog.shout() + "叫";
    }
}
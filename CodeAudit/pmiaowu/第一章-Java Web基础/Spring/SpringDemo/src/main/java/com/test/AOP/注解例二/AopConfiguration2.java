// 第三步
// 通过注解配置AOP功能,将房东与房屋中介联系起来
// 目录: ./SpringDemo/src/main/java/com/test/AOP/注解例二/
// 文件名: AopConfiguration2.java
package com.test.AOP.注解例二;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy // 开启基于注解的AOP模式(必须加)
@ComponentScan(basePackages = "com.test.AOP.注解例二")
public class AopConfiguration2 {
}
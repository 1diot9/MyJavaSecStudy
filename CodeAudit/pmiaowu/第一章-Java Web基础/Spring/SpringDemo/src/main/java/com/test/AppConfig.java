// 第一步
// @Configuration用于定义配置类,可替换xml配置文件
// 被注解的类内部包含有一个或多个被@Bean注解的方法
// 这些方法将会被AnnotationConfigApplicationContext或是AnnotationConfigWebApplicationContext类进行扫描
// 并用于构建bean定义,初始化Spring容器
// 目录: ./SpringDemo/src/main/java/com/test/
// 文件名: AppConfig.java
package com.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean(name = "helloWorldTest")
    public HelloWorld helloWorld() {
        HelloWorld h = new HelloWorld();
        h.setMessage("Hello World!");
        return h;
    }
}
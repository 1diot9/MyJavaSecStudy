// 第三步
// 在./SpringDemo/src/main/java/com/test/依赖注入/注解注入/创建个BeanConfig.java
// 目录: ./SpringDemo/src/main/java/com/test/依赖注入/注解注入/
// 文件名: BeanConfig.java
package com.test.依赖注入.注解注入;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    public BTest b() {
        return new BTest("张三", 16);
    }

    @Bean("aTest")
    public ATest a() {
        return new ATest(b(), "李四");
    }
}
// 第一步
// 引入注解来替换<context:component-scan>的xml配置,进行自动扫描,给予部分例子的配置
// 目录: ./SpringDemo/src/main/java/com/test/基于注解的配置/
// 文件名: AppContextConfig.java
package com.test.基于注解的配置;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.test.基于注解的配置")
public class AppContextConfig {
}
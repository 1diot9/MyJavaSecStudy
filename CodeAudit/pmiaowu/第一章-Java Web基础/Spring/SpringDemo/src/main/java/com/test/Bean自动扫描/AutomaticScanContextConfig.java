// 第一步
// 引入注解来替换<context:component-scan>的xml配置
// 目录: ./SpringDemo/src/main/java/com/test/Bean自动扫描/
// 文件名: AutomaticScanContextConfig.java
package com.test.Bean自动扫描;

import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.test.Bean自动扫描")
public class AutomaticScanContextConfig {
}
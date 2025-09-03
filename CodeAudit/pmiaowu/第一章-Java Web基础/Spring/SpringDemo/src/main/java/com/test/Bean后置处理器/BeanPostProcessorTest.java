// 第一步
// 创建个BeanPostProcessor后置处理器,进行测试
// 目录: ./SpringDemo/src/main/java/com/test/Bean后置处理器/
// 文件名: BeanPostProcessorTest.java
package com.test.Bean后置处理器;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class BeanPostProcessorTest implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("BeforeInitialization: " + beanName + " " + "正在执行预初始化方法");
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("AfterInitialization: " + beanName + " " + "正在执行后初始化方法");
        return bean;
    }
}

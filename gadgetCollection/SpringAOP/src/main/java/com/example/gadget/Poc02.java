package com.example.gadget;

import org.springframework.aop.aspectj.AspectJAroundAdvice;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.SingletonAspectInstanceFactory;
import org.springframework.aop.framework.ProxyFactory;

import java.lang.reflect.Method;


//toString--->anyMethod
//https://blog.potatowo.top/2025/03/31/%E4%BB%8E%E5%A4%8D%E7%8E%B0%E5%88%B0%E5%B0%9D%E8%AF%95%E7%94%A8tabby%E6%8C%96%E6%8E%98-SpringAOP%E9%93%BE/
public class Poc02 {
    public static void main(String[] args) throws Exception {



        Person person = new Person();
        Method declaredMethod = Person.class.getDeclaredMethod("getName");
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        SingletonAspectInstanceFactory instanceFactory = new SingletonAspectInstanceFactory(person);
        AspectJAroundAdvice aspectJAroundAdvice = new AspectJAroundAdvice(declaredMethod, pointcut, instanceFactory);
        ProxyFactory proxyFactory = new ProxyFactory(person);
        proxyFactory.addAdvice(aspectJAroundAdvice);

        Object proxy = proxyFactory.getProxy();
        proxy.toString();
//        Object o = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{Comparator.class},(InvocationHandler) proxy);


//        proxy.getName();
    }
}

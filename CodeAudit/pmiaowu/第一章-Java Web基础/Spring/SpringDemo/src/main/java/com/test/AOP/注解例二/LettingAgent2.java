// 第二步
// 创建个房屋中介类,处理各种重复并且比较边缘的事情
// 目录: ./SpringDemo/src/main/java/com/test/AOP/注解例二/
// 文件名: LettingAgent2.java
package com.test.AOP.注解例二;

import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component("lettingAgent2")
@Aspect
public class LettingAgent2 {
    @Pointcut(value = "execution(* com.test.AOP.注解例二.Landlord2.service())")
    private void pointCutTest() {
    }

    /**
     * 在所选方法执行之前
     */
    @Before(value = "pointCutTest()")
    public void before() {
        System.out.println("房屋中介,带租客看房");
        System.out.println("房屋中介,与租客谈价格");
    }

    /**
     * 在选定的方法执行之后
     */
    @After(value = "pointCutTest()")
    public void after() {
        System.out.println("房屋中介,准备将房间租赁合同上报国家相关单位");
    }

    /**
     * 当选定的方法返回时
     *
     * @param retVal
     */
    @AfterReturning(value = "pointCutTest()", returning = "retVal")
    public void afterReturning(Object retVal) {
        System.out.println("房屋中介,把钥匙给租客");
        System.out.println("房屋中介,交税给国家");
    }

    /**
     * 如果出现异常
     *
     * @param ex
     */
    @AfterThrowing(value = "pointCutTest()", throwing = "ex")
    public void afterThrowing(IllegalArgumentException ex) {
        System.out.println("房屋中介,意外事件处理-房东不想把房间租给这个人");
    }
}
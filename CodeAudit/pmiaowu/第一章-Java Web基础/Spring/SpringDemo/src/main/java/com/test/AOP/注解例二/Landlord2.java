// 第一步
// 创建个房东类,实现房东的核心的业务功能
// 目录: ./SpringDemo/src/main/java/com/test/AOP/注解例二/
// 文件名: Landlord2.java
package com.test.AOP.注解例二;

import org.springframework.stereotype.Component;

@Component("landlord2")
public class Landlord2 {
    private Boolean printThrow = false;

    public void setPrintThrow(Boolean printThrow) {
        this.printThrow = printThrow;
    }

    public Boolean getPrintThrow() {
        return this.printThrow;
    }

    /**
     * 实现房东的核心的业务功能
     */
    public void service() {
        if (this.getPrintThrow()) {
            this.printThrowException();
        }
        System.out.println("房东,和租客签合同");
        System.out.println("房东,收租客房租");
    }

    public void printThrowException() {
        throw new IllegalArgumentException();
    }
}
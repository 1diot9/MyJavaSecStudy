// 第一步
// 创建个基础父类,用来被其它类继承
// 目录: ./SpringDemo/src/main/java/com/test/Bean定义继承/
// 文件名: Hello.java
package com.test.Bean定义继承;

public class Hello {
    private String message1;
    private String message2;

    public void setMessage1(String message) {
        this.message1 = message;
    }

    public void setMessage2(String message) {
        this.message2 = message;
    }

    public void getMessage1() {
        System.out.println("Hello-Message1: " + message1);
    }

    public void getMessage2() {
        System.out.println("Hello-Message2: " + message2);
    }
}
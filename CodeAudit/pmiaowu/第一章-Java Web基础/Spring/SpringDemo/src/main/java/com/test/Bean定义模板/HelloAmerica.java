// 第一步
// 创建个普通类,准备用来继承Bean模板
// 目录: ./SpringDemo/src/main/java/com/test/Bean定义模板/
// 文件名: HelloAmerica.java
package com.test.Bean定义模板;

public class HelloAmerica {
    private String message1;
    private String message2;
    private String message3;

    public void setMessage1(String message) {
        this.message1 = message;
    }

    public void setMessage2(String message) {
        this.message2 = message;
    }

    public void setMessage3(String message) {
        this.message3 = message;
    }

    public void getMessage1() {
        System.out.println("HelloAmerica-Message1: " + message1);
    }

    public void getMessage2() {
        System.out.println("HelloAmerica-Message2: " + message2);
    }

    public void getMessage3() {
        System.out.println("HelloAmerica-Message3: " + message3);
    }
}
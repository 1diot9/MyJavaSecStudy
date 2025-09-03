// 第二步
// 创建个普通类,准备用来继承Hello类
// 目录: ./SpringDemo/src/main/java/com/test/Bean定义继承/
// 文件名: Hello.java
package com.test.Bean定义继承;

public class HelloChina {
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
        System.out.println("HelloChina-Message1: " + message1);
    }

    public void getMessage2() {
        System.out.println("HelloChina-Message2: " + message2);
    }

    public void getMessage3() {
        System.out.println("HelloChina-Message3: " + message3);
    }
}
// 第一步
// 创建个基础方法,拿来执行初始化与销毁时的动作
// 目录: ./SpringDemo/src/main/java/com/test/
// 文件名: HelloWorld3.java
package com.test;

public class HelloWorld3 {
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public void getMessage() {
        System.out.println("Your Message: " + message);
    }

    public void init() {
        System.out.println("这个bean进行了初始化");
    }

    public void destroy() {
        System.out.println("这个bean被销毁了");
    }
}
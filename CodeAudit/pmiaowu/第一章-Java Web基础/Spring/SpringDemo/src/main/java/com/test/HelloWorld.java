// 在./SpringDemo/src/main/java/com/test/创建个HelloWorld.java
// 目录: ./SpringDemo/src/main/java/com/test/
// 文件名: HelloWorld.java
package com.test;

public class HelloWorld {
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public void getMessage() {
        System.out.println("Your Message: " + message);
    }
}
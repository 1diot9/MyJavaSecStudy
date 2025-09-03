// 第一步
// 在./SpringDemo/src/main/java/com/test/创建个User.java
// 目录: ./SpringDemo/src/main/java/com/test/
// 文件名: User.java
package com.test;

public class User {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void show() {
        System.out.println("name: " + name);
    }
}
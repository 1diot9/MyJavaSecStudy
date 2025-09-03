// 第一步
// 在./SpringDemo/src/main/java/com/test/依赖注入/构造函数注入/User.java
// 目录: ./SpringDemo/src/main/java/com/test/依赖注入/构造函数注入/
// 文件名: User.java
package com.test.依赖注入.构造函数注入;

public class User {
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
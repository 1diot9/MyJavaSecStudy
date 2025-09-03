// 在./SpringDemo/src/main/java/com/test/依赖注入/构造函数注入/创建个User2.java
// 目录: ./SpringDemo/src/main/java/com/test/依赖注入/构造函数注入/
// 文件名: User2.java
package com.test.依赖注入.构造函数注入;

public class User2 {
    private String name;
    private Integer age;

    public User2(String name) {
        this.name = name;
    }

    public User2(Integer age) {
        this.age = age;
    }

    public User2(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return this.name;
    }

    public Integer getAge() {
        return this.age;
    }
}
// 第一步
// 在./SpringDemo/src/main/java/com/test/依赖注入/Set方式注入/创建个User3.java
// 目录: ./SpringDemo/src/main/java/com/test/依赖注入/Set方式注入/
// 文件名: User3.java
package com.test.依赖注入.Set方式注入;

public class User3 {
    private String name;
    private Integer age;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getAge() {
        return this.age;
    }
}
// 第一步
// 在./SpringDemo/src/main/java/com/test/依赖注入/Set方式注入/创建个Person.java
// 目录: ./SpringDemo/src/main/java/com/test/依赖注入/Set方式注入/
// 文件名: Person.java
package com.test.依赖注入.Set方式注入;

public class Person {
    private String name;
    private int age;

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

    @Override
    public String toString() {
        return "Person{" +
                "name='" + this.name + "', " +
                "age='" + this.age + "'" +
                "}";
    }
}
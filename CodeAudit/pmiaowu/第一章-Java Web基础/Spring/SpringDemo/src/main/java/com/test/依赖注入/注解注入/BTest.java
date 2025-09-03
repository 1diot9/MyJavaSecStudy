// 第一步
// 在./SpringDemo/src/main/java/com/test/依赖注入/注解注入/创建个BTest.java
// 目录: ./SpringDemo/src/main/java/com/test/依赖注入/注解注入/
// 文件名: BTest.java
package com.test.依赖注入.注解注入;

public class BTest {
    private String name;
    private Integer age;

    public BTest(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public void b() {
        System.out.println("BTest类中b()方法执行了,name=" + name + ",age=" + age);
    }
}
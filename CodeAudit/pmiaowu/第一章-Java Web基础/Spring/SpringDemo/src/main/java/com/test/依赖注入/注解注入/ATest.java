// 第二步
// 在./SpringDemo/src/main/java/com/test/依赖注入/注解注入/创建个ATest.java
// 目录: ./SpringDemo/src/main/java/com/test/依赖注入/注解注入/
// 文件名: ATest.java
package com.test.依赖注入.注解注入;

public class ATest {
    private BTest bTest;
    private String name;

    public ATest(BTest b, String name) {
        this.bTest = b;
        this.name = name;
    }

    public void a() {
        bTest.b();
        System.out.println("ATest中a()方法执行了, name=" + name);
    }
}
// 第三步
// 目录: ./SpringDemo/src/main/java/com/test/自动装配/例三/
// 文件名: Person3.java
package com.test.自动装配.例三;

public class Person3 {
    private String name;
    private Cat3 cat3;
    private Dog3 dog3;

    public Person3(String name, Cat3 cat3, Dog3 dog3) {
        this.name = name;
        this.cat3 = cat3;
        this.dog3 = dog3;
    }

    public String speak() {
        return "我叫" + this.name + "," +
                "有只猫会" + this.cat3.shout() + "叫," +
                "还有只狗会" + this.dog3.shout() + "叫";
    }
}
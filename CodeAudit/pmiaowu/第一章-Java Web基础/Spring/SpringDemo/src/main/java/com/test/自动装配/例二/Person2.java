// 第三步
// 目录: ./SpringDemo/src/main/java/com/test/自动装配/例二/
// 文件名: Person2.java
package com.test.自动装配.例二;

public class Person2 {
    private String name;
    private Cat2 cat2;
    private Dog2 dog2;

    public void setName(String name) {
        this.name = name;
    }

    public void setCat2(Cat2 cat2) {
        this.cat2 = cat2;
    }

    public void setDog2(Dog2 dog2) {
        this.dog2 = dog2;
    }

    public String speak() {
        return "我叫" + this.name + "," +
                "有只猫会" + this.cat2.shout() + "叫," +
                "还有只狗会" + this.dog2.shout() + "叫";
    }
}
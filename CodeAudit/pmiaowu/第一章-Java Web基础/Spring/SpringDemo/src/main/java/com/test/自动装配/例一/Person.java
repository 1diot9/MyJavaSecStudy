// 第三步
// 目录: ./SpringDemo/src/main/java/com/test/自动装配/例一/
// 文件名: Person.java
package com.test.自动装配.例一;

public class Person {
    private String name;
    private Cat cat;
    private Dog dog;

    public void setName(String name) {
        this.name = name;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    public String speak() {
        return "我叫" + this.name + "," +
                "有只猫会" + this.cat.shout() + "叫," +
                "还有只狗会" + this.dog.shout() + "叫";
    }
}
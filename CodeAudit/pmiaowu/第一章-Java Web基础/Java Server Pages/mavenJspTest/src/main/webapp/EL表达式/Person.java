// 文件地址: ./src/main/webapp/EL表达式/
// 文件名称: Person.java

package EL表达式;

public class Person {
    public String name;
    public int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
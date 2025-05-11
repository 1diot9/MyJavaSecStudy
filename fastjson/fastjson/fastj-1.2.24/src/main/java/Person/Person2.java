package Person;

import java.util.Properties;

//Properties只有get方法
public class Person2 {
    private String name;
    private int age;
    private Properties properties;

    public Person2(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Person2() {
        System.out.println("Person2 default constructor");
    }

    public String getName() {
        System.out.println("getName");
        return name;
    }

    public void setName(String name) {
        System.out.println("setName");
        this.name = name;
    }

    public int getAge() {
        System.out.println("getAge");
        return age;
    }

    public void setAge(int age) {
        System.out.println("setAge");
        this.age = age;
    }

    public Properties getProperties() {
        System.out.println("getProperties");
        return properties;
    }

}

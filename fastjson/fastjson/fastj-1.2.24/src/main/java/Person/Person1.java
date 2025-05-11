package Person;

import java.util.Properties;

//三个属性都有getter，setter
public class Person1 {
    private String name;
    private int age;
    private Properties properties;

    public Person1() {
        System.out.println("Person1 default constructor");
    }

    public Person1(String name, int age) {
        this.name = name;
        this.age = age;
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

    public void setProperties(Properties properties) {
        System.out.println("setProperties");
        this.properties = properties;
    }
}

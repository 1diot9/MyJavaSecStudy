package entity;

public abstract class Person {
    protected String name;
    protected int age;
    protected String profession;
    //子类无法访问，但依然继承
    private String secret;


    //设置成protected，只允许子类使用
    protected Person(String name, int age, String profession) {
        this.name = name;
        this.age = age;
        this.profession = profession;
    }

    protected void hello(String name) {
        System.out.println("hello " + name);
    }

    public abstract void test();

}

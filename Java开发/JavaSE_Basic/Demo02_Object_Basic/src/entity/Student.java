package entity;

import java.io.Serializable;

public class Student extends Person implements Study {

    private Status01 status;

    public Status01 getStatus() {
        return status;
    }

    public void setStatus(Status01 status) {
        this.status = status;
    }

    //子类在构造的时候需要初始化父类的属性，所以子类默认是调用了父类的构造方法
    //在无参的情况下可以省略
    public Student(String name, int age) {
        super(name, age, "student");
    }

    @Override
    public void test(){
        System.out.println("Student test");
    }

    @Override
    public void study() {
        System.out.println("Student study");
    }
}

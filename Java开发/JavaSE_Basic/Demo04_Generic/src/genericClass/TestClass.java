package genericClass;

public class TestClass<T extends Number> {
    private String name;
    private int age;
    private T grade;

    public TestClass(String name, int age, T grade) {
        this.name = name;
        this.age = age;
        this.grade = grade;
    }

    public <T> T test(T t){
        return t;
    }

    public T getGrade() {
        return grade;
    }

    public void setGrade(T grade) {
        this.grade = grade;
    }
}

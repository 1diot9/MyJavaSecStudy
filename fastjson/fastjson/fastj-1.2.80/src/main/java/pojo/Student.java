package pojo;

public class Student {
    public String name;
    public int age;
    public CharSequence charSequence;
    public Grades grades;

    public Student() {

    }

    public Student(Grades grades) {
        this.grades = grades;
    }

    public Student(CharSequence charSequence) {
        this.charSequence = charSequence;
    }

    public Student(String name, int age, CharSequence charSequence) {
        this.name = name;
        this.age = age;
        this.charSequence = charSequence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public CharSequence getCharSequence() {
        return charSequence;
    }

    public void setCharSequence(CharSequence charSequence) {
        this.charSequence = charSequence;
    }
}

import entity.Person;
import entity.Status;
import entity.Status01;
import entity.Student;

public class Main01 {
    public static void main(String[] args) {
        Student student = new Student("1diot9", 20);
        student.test();

        student.setStatus(Status01.Running);

        String name = student.getStatus().getName();
        System.out.println(name);

    }
}

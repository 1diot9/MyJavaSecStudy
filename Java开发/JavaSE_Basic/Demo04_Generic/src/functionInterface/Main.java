package functionInterface;

import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {
//        Supplier<Student> studentSupplier = new Supplier<Student>() {
//            @Override
//            public Student get() {
//                return new Student();
//            }
//        };

//        Supplier<Student> studentSupplier = () -> new Student();

        Supplier<Student> studentSupplier = Student::new;


        studentSupplier.get().hello();

    }


    public static class Student {
        public void hello(){
            System.out.println("hello");
        }
    }
}

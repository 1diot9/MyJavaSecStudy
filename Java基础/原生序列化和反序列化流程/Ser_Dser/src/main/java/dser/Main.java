package dser;

import java.io.*;
import java.util.Properties;

public class Main {
    public static class Demo implements Serializable {
        private String name;
        private int age;
        public String school;
        public Properties addr;

        public Demo() {
            System.out.println("无参构造");
            try {
                Runtime.getRuntime().exec("calc");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        static {
            System.out.println("static block");
        }


        public static void main(String[] args) throws IOException, ClassNotFoundException {
            Demo demo = new Demo();
            demo.setName("1diOt9");
            demo.setAge(10);
            demo.setSchool("bit");
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("test.ser"));
            oos.writeObject(demo);
            oos.close();

            ObjectInputStream ois = new ObjectInputStream((new FileInputStream("test.ser")));
            ois.readObject();
        }



        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }
    }
}

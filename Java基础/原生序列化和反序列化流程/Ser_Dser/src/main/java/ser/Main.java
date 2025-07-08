package ser;

import java.io.*;
import java.util.Properties;

public class Main {
    public static class Demo implements Serializable {
        private String name;
        private int age;
        public String school;
        public Properties addr;
        public Demo(String s) {
            this.name = s;
        }

        public static void main(String[] args) throws IOException, ClassNotFoundException {
            Demo demo = new Demo("1diOt9");
            demo.setAge(10);
            demo.setSchool("bit");
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("test.bin"));
            oos.writeObject(demo);
            oos.close();
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

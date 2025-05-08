import entity.Person;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Person person = new Person();
        person.test("cmd", "/c", "notepad");
        person.test01(new String[]{"cmd", "/c", "calc"});
    }
}
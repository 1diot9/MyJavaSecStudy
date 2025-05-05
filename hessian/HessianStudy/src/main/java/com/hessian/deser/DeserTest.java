package com.hessian.deser;

import com.caucho.hessian.io.Hessian2Input;
import com.hessian.pojo.Person;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DeserTest {
    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream("ser.bin");
        Hessian2Input hessian2Input = new Hessian2Input(fis);
        Person obj = (Person) hessian2Input.readObject();
        hessian2Input.close();
        System.out.println(obj.getClass().getName());
        System.out.println(obj.getAge());

    }
}

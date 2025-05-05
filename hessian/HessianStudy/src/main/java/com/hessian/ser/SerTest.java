package com.hessian.ser;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.hessian.pojo.Person;

import java.io.*;
import java.util.HashMap;

public class SerTest {
    public static void main(String[] args) throws IOException {
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("name", "maxyyy");

        Person person = new Person("1diOt9", 20);
//        FileOutputStream fos = new FileOutputStream("ser.bin");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Hessian2Output output = new Hessian2Output(baos);
        output.writeObject(hashMap);
        output.close();
        byte[] bytes = baos.toByteArray();

        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        Hessian2Input input = new Hessian2Input(bais);
        input.readObject();
        input.close();
    }
}

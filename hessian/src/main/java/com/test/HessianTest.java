package com.test;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.test.pojo.Person;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class HessianTest {
    public static void main(String[] args) throws IOException {
        Person baka = new Person(1, "baka");

        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("name", "baka");

        byte[] bytes = hessianSer2bytes(hashMap);
        Object o = hessianDeser(bytes);

    }

    public static byte[] hessian2Ser2bytes(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Hessian2Output hessian2Output = new Hessian2Output(baos);
        hessian2Output.writeObject(obj);
        hessian2Output.close();
        return baos.toByteArray();
    }

    public static Object hessian2Deser(byte[] bytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        Hessian2Input hessian2Input = new Hessian2Input(bais);
        Object o = hessian2Input.readObject();
        hessian2Input.close();
        return o;
    }

    public static byte[] hessianSer2bytes(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HessianOutput hessianOutput = new HessianOutput(baos);
        hessianOutput.writeObject(obj);
        hessianOutput.close();
        return baos.toByteArray();
    }

    public static Object hessianDeser(byte[] bytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        HessianInput hessianInput = new HessianInput(bais);
        Object o = hessianInput.readObject();
        hessianInput.close();
        return o;
    }
}

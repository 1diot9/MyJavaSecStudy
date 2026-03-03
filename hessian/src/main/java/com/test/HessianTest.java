package com.test;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.test.pojo.Person;
import tools.HessianTools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class HessianTest {
    public static void main(String[] args) throws IOException {
        Person baka = new Person(1, "baka");

        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("name", "baka");

        byte[] bytes = HessianTools.hessianSer2bytes(baka, "2");
        Object o = HessianTools.hessianDeser(bytes, "2");

    }
}

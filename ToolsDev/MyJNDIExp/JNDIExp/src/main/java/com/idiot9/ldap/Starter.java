package com.idiot9.ldap;

import com.idiot9.ldap.gadgets.GadgetFactory;
import com.idiot9.ldap.utils.Config;
import com.idiot9.ldap.utils.EncodeFactory;
import com.idiot9.ldap.utils.PayloadGenerator;

public class Starter {
    public static void main(String[] args) throws Exception {
//        args = new String[]{"-s", "Test", "-e", "bin"};
        Config.applyArgs(args);

        String s = PayloadGenerator.generatePayload(Config.gadget, Config.cmd, Config.encode, Config.mem, Config.password);
        System.out.println(s);
    }
}

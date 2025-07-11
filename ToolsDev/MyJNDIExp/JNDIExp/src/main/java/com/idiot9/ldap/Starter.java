package com.idiot9.ldap;

import com.idiot9.ldap.gadgets.GadgetFactory;
import com.idiot9.ldap.utils.Config;
import com.idiot9.ldap.utils.EncodeFactory;
import com.idiot9.ldap.utils.PayloadGenerator;
import com.idiot9.springboot.WebApplication;

public class Starter {
    public static void main(String[] args) throws Exception {
//        args = new String[]{"-u"};
        Config.applyArgs(args);

        if (Config.sp){
            WebApplication.start();
        }

        if (Config.gadget != null){
            String s = PayloadGenerator.generatePayload(Config.gadget, Config.cmd, Config.classFile, Config.encode, Config.mem, Config.password);
            System.out.println(s);
        }

        if (Config.ip != null) {
            LdapServer.start();
            HTTPServer.start();
        }
    }
}

package com.idiot9.ldap.gadgets;

import javax.xml.transform.Templates;

public class Test implements GadgetFactory {
    @Override
    public Object getGadget(Templates templates) throws Exception {
        return null;
    }

    @Override
    public Object getCmdObject(String cmd) throws Exception {
        return null;
    }

    @Override
    public Object getMemObject(String mem, String password) throws Exception {
        return null;
    }
}

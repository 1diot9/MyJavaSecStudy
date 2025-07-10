package com.idiot9.ldap.templates;

import javassist.CannotCompileException;

import javax.xml.transform.Templates;

public class SpringEchoTemplates implements TemplateFactory{
    @Override
    public void createTemplatesImpl() throws CannotCompileException, Exception {

    }

    @Override
    public String getClassName() {
        return "";
    }

    @Override
    public Templates getTemplates() {
        return null;
    }

    @Override
    public void cache() throws Exception {

    }

    @Override
    public byte[] getBytes() {
        return new byte[0];
    }
}

package com.idiot9.ldap.templates;

import com.idiot9.ldap.utils.Cache;
import javassist.CannotCompileException;

import javax.xml.transform.Templates;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ClassFileTemplates implements TemplateFactory{
    private byte[] bytes;
    private Templates templates;
    private String classFile;
    //注意，这里默认文件名和类名一样
    private String className;


    public ClassFileTemplates(String classFile) throws Exception {
        this.classFile = classFile;
        this.className = classFile.substring(0, classFile.lastIndexOf("."));
        createTemplatesImpl();
    }

    @Override
    public void createTemplatesImpl() throws CannotCompileException, Exception {
        Templates templates = (Templates) tplClass.newInstance();
        this.bytes = Files.readAllBytes(Paths.get(this.classFile));
        this.templates = TemplateFactory.loadBytes(this.bytes, templates);
    }

    @Override
    public String getClassName() {
        return this.className;
    }

    @Override
    public Templates getTemplates() {
        return this.templates;
    }

    @Override
    public void cache() throws Exception {
        Cache.set(this.className, this.bytes);
    }

    @Override
    public byte[] getBytes() {
        return this.bytes;
    }
}

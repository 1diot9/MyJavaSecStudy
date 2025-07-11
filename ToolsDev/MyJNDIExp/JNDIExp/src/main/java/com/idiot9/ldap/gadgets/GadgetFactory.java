package com.idiot9.ldap.gadgets;

import com.idiot9.ldap.templates.MemShell;
import com.idiot9.ldap.templates.TemplateFactory;
import org.reflections.Reflections;

import javax.xml.transform.Templates;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Set;

public interface GadgetFactory {
//    public byte[] getBytes() throws Exception;

    //链子的主要逻辑
    public Object getGadget(Templates templates) throws Exception;

    public Object getCmdObject(String cmd) throws Exception;

    public Object getMemObject(String mem, String password) throws Exception;

    public Object getClassFileObject(String classFile) throws Exception;



    public static class Utils{
        //通过Reflection依赖进行包扫描，比起通过Java原生实现更加简单
        public static Set<Class<? extends GadgetFactory>> getGadgetClasses() {
            final Reflections reflections = new Reflections(GadgetFactory.class.getPackage().getName());
            //获取GadgetFactory的实现，放入集合中
            final Set<Class<? extends GadgetFactory>> GadgetTypes = reflections.getSubTypesOf(GadgetFactory.class);
            //对所有子类进行遍历
            for (Iterator<Class<? extends GadgetFactory>> iterator = GadgetTypes.iterator(); iterator.hasNext(); ) {
                Class<? extends GadgetFactory> pc = iterator.next();
                //移除子类中的接口类和抽象类
                if ( pc.isInterface() || Modifier.isAbstract(pc.getModifiers()) ) {
                    iterator.remove();
                }
            }
            return GadgetTypes;
        }

        //获取所有有内存马注解的类，方便列出可用内存马
        public static Set<Class<?>> getMemClasses() {
            final Reflections reflections = new Reflections(TemplateFactory.class.getPackage().getName());
            //获取GadgetFactory的实现，放入集合中
            final Set<Class<?>> memTypes =  reflections.getTypesAnnotatedWith(MemShell.class);
            //对所有子类进行遍历
            for (Iterator<Class<?>> iterator = memTypes.iterator(); iterator.hasNext(); ) {
                Class<? extends TemplateFactory> pc = (Class<? extends TemplateFactory>) iterator.next();
                //移除子类中的接口类和抽象类
                if ( pc.isInterface() || Modifier.isAbstract(pc.getModifiers()) ) {
                    iterator.remove();
                }
            }
            return memTypes;
        }

        //通过gadget名称得到gadget的Class对象，即通过String参数获取对应类
        public static Class<? extends GadgetFactory> getGadgetClass(String gadgetName){
            Class<? extends GadgetFactory> clazz = null;
            String packageName = GadgetFactory.class.getPackage().getName();
            try {
                clazz = (Class<? extends GadgetFactory>) Class.forName(packageName + "." + gadgetName);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if(clazz == null){
                System.err.println("Gadget not found: " + gadgetName);
            }
            return clazz;
        }

        public static Object getGadgetObject(Class<? extends GadgetFactory> clazz, String classFile, String cmd, String mem, String password) throws Exception {
            Object obj = null;
            GadgetFactory gadgetFactory = clazz.newInstance();
            if (classFile != null) {
                obj = gadgetFactory.getClassFileObject(classFile);
            }else if (mem != null) {
                obj = gadgetFactory.getMemObject(mem, password);
            }else{
                obj = gadgetFactory.getCmdObject(cmd);
            }
            return obj;
        }
    }
}

package com.idiot9.ldap.gadgets;

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



    public static class Utils{
        //通过Reflection依赖进行包扫描，比起通过Java原生实现更加简单
        public static Set<Class<? extends GadgetFactory>> getPayloadClasses () {
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

        public static Object getGadgetObject(Class<? extends GadgetFactory> clazz, String cmd, String mem, String password) throws Exception {
            Object obj = null;
            GadgetFactory gadgetFactory = clazz.newInstance();
            if (mem == null) {
                obj = gadgetFactory.getCmdObject(cmd);
            }else {
                obj = gadgetFactory.getMemObject(mem, password);
            }
            return obj;
        }
    }
}

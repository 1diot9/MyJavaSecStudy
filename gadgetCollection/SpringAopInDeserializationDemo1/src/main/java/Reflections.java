import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Reflections {

    public static Field getField(Class clazz,String fieldName) throws NoSuchFieldException {

        while (true){
            Field[] fields = clazz.getDeclaredFields();
            for(Field field:fields){
                if(field.getName().equals(fieldName)){
                    return field;
                }
            }
            if(clazz == Object.class){
                break;
            }
            clazz = clazz.getSuperclass();
        }
        throw new NoSuchFieldException(fieldName);
    }


    public static Object getFieldValue(Object obj,String filedName) throws NoSuchFieldException, IllegalAccessException {
        Field field = getField(obj.getClass(),filedName);
        field.setAccessible(true);
        return field.get(obj);
    }

    public static void setFieldValue(Object obj,String filedName,Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = getField(obj.getClass(),filedName);
        field.setAccessible(true);
        field.set(obj,value);
    }

    public static Method getMethod(Class clazz,String methodName,Class... paramTypes) throws NoSuchFieldException {

        while (true){
            Method[] methods = clazz.getDeclaredMethods();
            for(Method method:methods){
                if(method.getName().equals(methodName) && Arrays.equals(paramTypes, method.getParameterTypes())){
                    method.setAccessible(true);
                    return method;
                }
            }
            if(clazz == Object.class){
                break;
            }
            clazz = clazz.getSuperclass();
        }
        throw new NoSuchFieldException(methodName);
    }


    public static boolean hasField(Class clazz,String fieldName) {
        try {
            getField(clazz,fieldName);
            return true;
        }catch (Exception e){
            return false;
        }
    }


    public static Object newInstance(String className,Class paramType,Object param) throws Exception {
        return newInstance(Class.forName(className),paramType,param);
    }

    public static Object newInstance(String className,Class[] paramTypes,Object[] params) throws Exception {
        return newInstance(Class.forName(className),paramTypes,params);
    }

    public static Object newInstance(Class clazz,Class paramType,Object param) throws Exception {
        return newInstance(clazz,new Class[]{paramType},new Object[]{param});
    }

    public static Object newInstance(Class clazz,Class[] paramTypes,Object[] params) throws Exception {
        Constructor constructor = clazz.getDeclaredConstructor(paramTypes);
        constructor.setAccessible(true);
        return constructor.newInstance(params);
    }

    public static Object newInstanceWithOnlyConstructor(String className,Object... params) throws Exception {
        return newInstanceWithOnlyConstructor(Class.forName(className),params);
    }

    public static Object newInstanceWithOnlyConstructor(Class clazz,Object... params) throws Exception {
        Constructor[] constructors = clazz.getDeclaredConstructors();
        if(constructors.length > 1){
            throw new IllegalStateException("The number of construction methods is more than 1,can't use newInstanceWithOnlyConstructor");
        }
        Constructor constructor = constructors[0];
        constructor.setAccessible(true);
        return constructor.newInstance(params);
    }

    public static Object newInstanceWithoutConstructor(String className) throws Exception {
        return newInstanceWithoutConstructor(Class.forName(className));
    }

    public static <T> T newInstanceWithoutConstructor(Class<T> clazz) throws Exception {
        return newInstanceWithSpecialConstructor(clazz,Object.class,new Class[0],new Object[0]);
    }

    public static <T> T newInstanceWithSpecialConstructor( Class<T> clazz, Class constructorClass, Class<?>[] consArgTypes, Object[] consArgs) throws Exception {
        Constructor consTmpl = constructorClass.getDeclaredConstructor(consArgTypes);
        consTmpl.setAccessible(true);
        Constructor constructor = ReflectionFactory.getReflectionFactory().newConstructorForSerialization(clazz, consTmpl);
        constructor.setAccessible(true);
        return (T)constructor.newInstance(consArgs);
    }


    /**
     * @description     删除某个类的特定方法，用于在特定场景NOP掉某些检查，例如jackson的pojonocde gadget
     *                  在序列化时会调用父类的writeResplace方法，需要让该方法无效才能正常生成payload
     *                  实际做法：由于javassist不允许直接删除方法，可以修改方法达成删除一样的效果
     *
     * @return          修改后的方法名，方便后续恢复回来
     */
    public static String DeleteMethod(String className,String methodName,String methodDesc) throws Exception {
        String newName = methodName + "RenameToRemove";
        return RenameMethod(className,methodName,methodDesc,newName);
    }


    public static Method[] getGetterMethod(Class clazz) throws Exception{
        Method[] allMethods = clazz.getMethods();
        List methods = new ArrayList();
        for(Method method:allMethods){
            String name = method.getName();
            if(name.length()>3 && name.startsWith("get") && Character.isUpperCase(name.charAt(3)) &&
                    method.getParameterCount()==0 ){
                methods.add(method);
            }
        }
        return (Method[]) methods.toArray(new Method[methods.size()]);
    }

    /**
     * @description     重命名方法
     * @return          修改后的方法名，方便后续恢复回来
     */
    public static String RenameMethod(String className,String methodName,String methodDesc,String newName) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get(className);
        CtMethod cm = cc.getMethod(methodName,methodDesc);
        cm.setName(newName);
        cc.toClass();
        cc.detach();
        return newName;
    }

    /**
     * 根据类名获取类，处理一些特殊情况
     */
    public static Class getClassByName(String className) throws ClassNotFoundException {
        Class clazz = null;
        className = className.replace("/",".");
        try {
            clazz = Class.forName(className);
        }catch (Exception e1){
            String newClassName;
            // 处理内部类的情况
            int[] positions = Util.findAllPositionsOfChar(className,'.');
            int len = className.length();
            for(int i=positions.length-1;i>=0;i--){
                int position = positions[i];
                newClassName = className.substring(0,position) + "$" + className.substring(position+1,len);
                try {
                    clazz = Class.forName(newClassName);

                }catch (Exception e2){

                }
            }
        }
        if (clazz == null) throw new ClassNotFoundException(className);

        return clazz;
    }

}
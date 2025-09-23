package IdiOt9.Tools;

import sun.misc.Unsafe;

import java.io.*;
import java.lang.reflect.Field;

public class Tools17 {

    public static void bypassModule(Class aClass) throws Exception{
        Class unsafeClass = Class.forName("sun.misc.Unsafe");
        Field field = unsafeClass.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe) field.get(null);

        Field modules = Class.class.getDeclaredField("module");
        long l = unsafe.objectFieldOffset(modules);
        unsafe.getAndSetObject(aClass, l, Object.class.getModule());
    }

    public static byte[] ser(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.close();
        return baos.toByteArray();
    }

    public static Object deser(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais);
        return ois.readObject();
    }

    public static void setFieldValue(Object obj, String fieldName, Object value) throws IllegalAccessException {
        Class<?> aClass = obj.getClass();
        Field field = null;
        while (aClass != null) {
            try {
                field = aClass.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                aClass = aClass.getSuperclass();
            }
        }
        field.setAccessible(true);
        field.set(obj, value);
    }

    public static Object getFieldValue(Object obj, String fieldName){
        Class<?> aClass = obj.getClass();
        Field field = null;
        while (aClass != null) {
            try {
                field = aClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(obj);
            } catch (NoSuchFieldException e) {
                aClass = aClass.getSuperclass();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}

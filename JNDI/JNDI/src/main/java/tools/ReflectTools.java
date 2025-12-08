package tools;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Base64;

public class ReflectTools {
    public static void setFieldValue(Object obj, String fieldName, Object value) throws IllegalAccessException {
        Class<?> aClass = obj.getClass();
        Field field = null;
        try {
            field = aClass.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            aClass = aClass.getSuperclass();
        }
        field.setAccessible(true);
        field.set(obj, value);
    }

    // 生成不继承Serializable接口的Class，防止因suid不一样报错
    public static Class makeClass(String className, String suid) throws ClassNotFoundException, CannotCompileException {
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.makeClass(className);

        if (suid != null) {
            // 添加 serialVersionUID 字段并指定其值
            CtField serialVersionUIDField = new CtField(CtClass.longType, "serialVersionUID", ctClass);
            serialVersionUIDField.setModifiers(Modifier.PRIVATE | Modifier.STATIC | Modifier.FINAL);  // 设置为private static final
            ctClass.addField(serialVersionUIDField, suid);  // 设置 serialVersionUID 值为 1L
        }

        Class<?> aClass = ctClass.toClass();
        return aClass;
    }

    public static byte[] ser2bytes(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        return baos.toByteArray();
    }

    public static void ser2file(Object obj, String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(obj);
    }

    public static Object deser(byte[] bytes, String base64) throws IOException, ClassNotFoundException {
        if (bytes != null) {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        }else {
            byte[] decode = Base64.getDecoder().decode(base64);
            ByteArrayInputStream bais = new ByteArrayInputStream(decode);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        }
    }


}

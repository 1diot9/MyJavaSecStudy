package exp.tools;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;

import javax.swing.undo.CompoundEdit;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Base64;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ReflectTools {
    // 方便加虚拟机选项
    public static void main(String[] args) {

    }

    public static void setFieldValue(Object obj, String fieldName, Object value) throws IllegalAccessException {
        Class<?> aClass = obj.getClass();
        Field field = null;
        while (aClass != null){
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

    // 修改static final 字段
    public static void setFinalField(Object object, String fieldName, Object newValue) throws Exception {
        // 获取指定类的 Class 对象
        Class<?> clazz = object.getClass();

        // 获取 private final 字段
        Field field = clazz.getDeclaredField(fieldName);

        // 使私有字段可以访问
        field.setAccessible(true);

        // 移除 final 修饰符
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        // 修改字段的值
        field.set(object, newValue);
    }

    public static Object getFieldValue(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        try {
            UnsafeTools.patchModule(ReflectTools.class, CompoundEdit.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Class<?> aClass = obj.getClass();

        while (aClass != null){
            try{
                Field declaredField = aClass.getDeclaredField(fieldName);
                declaredField.setAccessible(true);
                return declaredField.get(obj);
            }catch(NoSuchFieldException e){
                aClass = aClass.getSuperclass();
            }
        }
        return null;
    }

    public static HashMap<Object, Object> makeMap (Object v1, Object v2 ) throws Exception {
        HashMap<Object, Object> s = new HashMap<>();
        setFieldValue(s, "size", 2);
        Class<?> nodeC;
        try {
            nodeC = Class.forName("java.util.HashMap$Node");
        }
        catch ( ClassNotFoundException e ) {
            nodeC = Class.forName("java.util.HashMap$Entry");
        }
        Constructor<?> nodeCons = nodeC.getDeclaredConstructor(int.class, Object.class, Object.class, nodeC);
        nodeCons.setAccessible(true);
        Object tbl = Array.newInstance(nodeC, 2);
        Array.set(tbl, 0, nodeCons.newInstance(0, v1, v1, null));
        Array.set(tbl, 1, nodeCons.newInstance(0, v2, v2, null));
        setFieldValue(s, "table", tbl);
        return s;
    }

    // 制作hash冲突map，实现调用called.equals(param)
    public static HashMap<Object, Object> makeEqualMap(Object called, Object param) throws Exception {
        HashMap<Object, Object> hashMap1 = new HashMap<>();
        HashMap<Object, Object> hashMap2 = new HashMap<>();
        hashMap1.put("zZ", called);
        hashMap1.put("yy", param);
        hashMap2.put("zZ", param);
        hashMap2.put("yy", called);

        HashMap<Object, Object> finalMap = makeMap(hashMap2, hashMap1);

        return finalMap;
    }

    public static ConcurrentHashMap<Object, Object> makeConcurrentMap (Object v1, Object v2 ) throws Exception {
        ConcurrentHashMap s = new ConcurrentHashMap();
        setFieldValue(s, "sizeCtl", 2);
        Class nodeC;
        try {
            nodeC = Class.forName("java.util.concurrent.ConcurrentHashMap$Node");
        }
        catch ( ClassNotFoundException e ) {
            nodeC = Class.forName("java.util.concurrent.ConcurrentHashMap$Entry");
        }
        Constructor nodeCons = nodeC.getDeclaredConstructor(int.class, Object.class, Object.class, nodeC);
        nodeCons.setAccessible(true);
        Object tbl = Array.newInstance(nodeC, 2);
        Array.set(tbl, 0, nodeCons.newInstance(0, v1, v1, null));
        Array.set(tbl, 1, nodeCons.newInstance(0, v2, v2, null));
        setFieldValue(s, "table", tbl);
        Field table = ConcurrentHashMap.class.getDeclaredField("table");
        table.setAccessible(true);
        table.set(s, tbl);

        return s;
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

    // 用于在HashMap中制造hash碰撞，从而触发equals，用于XString系列的equals触发；有时会失败，如XBean链，原因未知
    public static String unhash ( int hash ) {
        int target = hash;
        StringBuilder answer = new StringBuilder();
        if ( target < 0 ) {
            // String with hash of Integer.MIN_VALUE, 0x80000000
            answer.append("\u0915\u0009\u001e\u000c\u0002");

            if ( target == Integer.MIN_VALUE )
                return answer.toString();
            // Find target without sign bit set
            target = target & Integer.MAX_VALUE;
        }

        unhash0(answer, target);
        return answer.toString();
    }
    private static void unhash0 ( StringBuilder partial, int target ) {
        int div = target / 31;
        int rem = target % 31;

        if ( div <= Character.MAX_VALUE ) {
            if ( div != 0 )
                partial.append((char) div);
            partial.append((char) rem);
        }
        else {
            unhash0(partial, div);
            partial.append((char) rem);
        }
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

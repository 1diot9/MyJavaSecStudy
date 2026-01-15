package tools;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeTools {
    // 绕过构造方法获取对象
    public static Object getObjectByUnsafe(Class clazz) throws Exception{
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        Unsafe unsafe = (Unsafe) theUnsafe.get(null);
        return unsafe.allocateInstance(clazz);
    }
}

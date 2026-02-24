package tools;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
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

    // --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.xml/com.sun.org.apache.bcel.internal.classfile=ALL-UNNAMED --add-opens=java.xml/com.sun.org.apache.xalan.internal.xsltc.trax=ALL-UNNAMED
//    public static void patchModule(Class current, Class target) throws Exception {
//        Field field = Unsafe.class.getDeclaredField("theUnsafe");
//        field.setAccessible(true);
//        Unsafe unsafe = (Unsafe) field.get(null);
//
//        // 所有Class的数据结构都是一样的，相同字段的偏移量也是一样的
//        long l = unsafe.objectFieldOffset(Class.class.getDeclaredField("module"));
//        Module targetModule = target.getModule();
//        unsafe.putObject(current, l, targetModule);
//    }
}

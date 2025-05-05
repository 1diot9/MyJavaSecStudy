package DynamicClassLoader;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;

public class defineClass {
    public static void main(String[] args) throws NoSuchMethodException, IOException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        //从当前进程获取类加载器
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        //读取字节码
        byte[] bytes = Files.readAllBytes(Paths.get("D://1tmp//classes//Calc.class"));
        //通过反射获取defineClass方法，将字节码加载到JVM中
        Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
        defineClass.setAccessible(true);
        defineClass.invoke(classLoader, "Calc", bytes, 0, bytes.length);
        //实例化刚刚通过字节码加载的类，这里会调用静态代码块和无参构造
        Class.forName("Calc").newInstance();
    }
}

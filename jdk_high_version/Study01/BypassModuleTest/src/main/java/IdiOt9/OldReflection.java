package IdiOt9;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;

// module java.base does not "opens java.lang" to unnamed module @6f496d9f
public class OldReflection {
    public static void main(String[] args) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        byte[] bytes = Files.readAllBytes(Paths.get("D://1tmp//classes//Calc.class"));

        Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
        defineClass.setAccessible(true);

        Class calc = (Class) defineClass.invoke(ClassLoader.getSystemClassLoader(), "Calc", bytes, 0, bytes.length);
        calc.newInstance();
    }
}

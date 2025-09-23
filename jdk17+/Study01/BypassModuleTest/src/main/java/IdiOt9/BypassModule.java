package IdiOt9;




import IdiOt9.Tools.Tools17;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BypassModule {
    public static void main(String[] args) throws Exception {


        Tools17.bypassModule(BypassModule.class);
        byte[] bytes = Files.readAllBytes(Paths.get("D://1tmp//classes//Calc.class"));


        Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
        defineClass.setAccessible(true);

        Class calc = (Class) defineClass.invoke(ClassLoader.getSystemClassLoader(), "Calc", bytes, 0, bytes.length);
        calc.newInstance();


    }
}

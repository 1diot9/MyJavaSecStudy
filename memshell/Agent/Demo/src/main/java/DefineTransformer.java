import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

// 每当类被加载，就会调用 transform 函数
public class DefineTransformer implements ClassFileTransformer {

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        System.out.println("DefineTransformer invoked: " + className);
        className = className.replace('/', '.');
        if (className.equals("Hello") || className.equals("AgentMainDemo")) {
            System.out.println("DefineTransformer find: " + className);
            ClassPool pool = ClassPool.getDefault();
            try {
                CtClass ctClass = pool.getCtClass(className);
                CtMethod main = ctClass.getDeclaredMethod("main");
                System.out.println("start insert...");
                main.insertBefore("System.out.println(\"\\ninsert success!!!\\n\");");
                byte[] bytecode = ctClass.toBytecode();
                // 将 c 从 classpool 中删除以释放内存
                ctClass.detach();
                return bytecode;
            } catch (NotFoundException | CannotCompileException | IOException e) {
                throw new RuntimeException(e);
            }

        }
        return new byte[0];
    }
}
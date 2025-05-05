package Tools;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class Class2Base64 {
    public static void main(String[] args) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get("D:\\BaiduSyncdisk\\code\\MyJavaSecStudy\\memshell\\SpringBoot\\Controller_Interceptor\\target\\classes\\POC\\AddBadController.class"));
        String s = Base64.getEncoder().encodeToString(bytes);
        new FileOutputStream("D://1tmp//memshell//Spring_Controller//BadController_bytes.txt").write(s.getBytes());
    }
}

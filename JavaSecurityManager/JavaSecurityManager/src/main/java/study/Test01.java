package study;

import java.io.IOException;

public class Test01 {
    public static void main(String[] args) throws IOException {
        SecurityManager securityManager = new SecurityManager();
        System.setSecurityManager(securityManager);
        Runtime.getRuntime().exec("calc");

    }
}

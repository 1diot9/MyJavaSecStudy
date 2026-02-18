package pojo;

import java.io.IOException;

public class EvilException extends Exception {
    public void setCode(String code) {
        System.out.println("start!!!");
        ProcessBuilder pb = new ProcessBuilder(code);
        try {
            pb.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

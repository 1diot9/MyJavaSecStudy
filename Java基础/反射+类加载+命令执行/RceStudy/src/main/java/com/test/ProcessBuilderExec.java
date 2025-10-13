package com.test;

import java.io.IOException;

public class ProcessBuilderExec {
    public static void main(String[] args) throws IOException {
        String[] cmd = {"cmd", "/c", "calc"};
        ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        processBuilder.start();
    }
}

package com.test.study;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class RuntimeExec {
    public static void main(String[] args) throws IOException {
        InputStream inputStream = Runtime.getRuntime().exec("whoami && whoami").getInputStream();
        System.out.println(IOUtils.toString(inputStream));
        inputStream = Runtime.getRuntime().exec(new String[]{"whoami","&&","whoami"}).getInputStream();
        System.out.println(IOUtils.toString(inputStream));
        inputStream = Runtime.getRuntime().exec(new String[]{"whoami && whoami"}).getInputStream();
        System.out.println(IOUtils.toString(inputStream));
        inputStream = Runtime.getRuntime().exec(new String[]{"cmd.exe","/c","whoami && whoami"}).getInputStream();
        System.out.println(IOUtils.toString(inputStream));
//        inputStream = Runtime.getRuntime().exec("echo 1 > 1.txt").getInputStream();
//        System.out.println(IOUtils.toString(inputStream));
//        inputStream = Runtime.getRuntime().exec("powershell.exe -NonI -W Hidden -NoP -Exec Bypass -Enc ZQBjAGgAbwAgADEAIAA+ACAAMQAuAHQAeAB0AA==").getInputStream();
    }
}

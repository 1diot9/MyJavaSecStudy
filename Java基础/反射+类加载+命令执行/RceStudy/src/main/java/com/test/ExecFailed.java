package com.test;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class ExecFailed {
    public static void main(String[] args) throws IOException {
        String origin = "echo 123";
        String evil = "|echo 321";
        String cmd = origin + evil;
        InputStream inputStream = Runtime.getRuntime().exec(cmd).getInputStream();
        System.out.printf(IOUtils.toString(inputStream));
        // 123|echo 321
        // 以空格分割，空格后全是参数，所以管道符没用
    }
}

package com.test.study;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class tmp {
    public static void main(String[] args) throws IOException {
//        Runtime runtime = new Runtime();
//        runtime.exec("calc");
        Process exec = Runtime.getRuntime().exec("whoami");
        InputStream inputStream = exec.getInputStream();
        System.out.println(IOUtils.toString(inputStream));
    }
}

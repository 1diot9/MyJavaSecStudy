package com.ssm_project.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CommandServiceImpl implements CommandService{
    @Override
    public String exec(String cmd) {
        StringBuffer stringBuffer = new StringBuffer();
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream inputStream = process.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = null;

        while (true){
            try {
                if (!((line = bufferedReader.readLine())!= null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            stringBuffer.append(line).append("\n");

        }
        System.out.println(stringBuffer.toString());
        return stringBuffer.toString();
    }
}

package com.idiot9.ldap.utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EncodeFactory {
    public static String encode(byte[] ser, String encode) {
        String result = null;
        switch (encode) {
            case "bin":
                result = new String(ser, StandardCharsets.UTF_8);
                break;
            case "base64":
                result = Base64.getEncoder().encodeToString(ser);
                break;
            case "base64url":
                result = Base64.getEncoder().encodeToString(ser);
                result = URLEncoder.encode(result);
                break;
            case "hex":
                StringBuffer sb = new StringBuffer();
                for (byte b : ser){
                    sb.append(String.format("%02X", b));
                }
                result = sb.toString();
                break;
        }
        return result;
    }
}

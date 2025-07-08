package ysoserial.payloads.encode;

import java.util.Base64;

public class Base64EncodeFactory implements EncodeFactory<String> {
    @Override
    public String encode(byte[] data) throws Exception {
        String s = Base64.getEncoder().encodeToString(data);
        return s;
    }
}

package ysoserial.payloads.encode;

public class BytesEncodeFactory implements EncodeFactory {
    @Override
    public Object encode(byte[] data) throws Exception {
        return new String(data);
    }
}

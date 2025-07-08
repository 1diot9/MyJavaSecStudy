package ysoserial.payloads.encode;

import ysoserial.GeneratePayload;

public interface EncodeFactory<T> {
    public T encode(byte[] data) throws Exception;

    public static class Utils{
        public static Class<? extends EncodeFactory> getEncodeClass(final String encodeType) {
            Class<? extends EncodeFactory> clazz = null;
            try {
                clazz = (Class<? extends EncodeFactory>) Class.forName(GeneratePayload.class.getPackage().getName() + ".payloads.encode." + encodeType + "EncodeFactory");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            return clazz;
        }
    }
}

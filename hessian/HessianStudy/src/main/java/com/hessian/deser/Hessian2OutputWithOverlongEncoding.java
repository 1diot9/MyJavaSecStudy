package com.hessian.deser;

import com.caucho.hessian.io.Hessian2Output;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;

public class Hessian2OutputWithOverlongEncoding extends Hessian2Output {
    public Hessian2OutputWithOverlongEncoding(OutputStream os) {
        super(os);
    }

    @Override
    public void printString(String v, int strOffset, int length) throws IOException {
        int offset = (int) getSuperFieldValue("_offset");
        byte[] buffer = (byte[]) getSuperFieldValue("_buffer");

        for (int i = 0; i < length; i++) {
            if (SIZE <= offset + 16) {
                setSuperFieldValue("_offset", offset);
                flushBuffer();
                offset = (int) getSuperFieldValue("_offset");
            }

            char ch = v.charAt(i + strOffset);

            // 2 bytes UTF-8
            buffer[offset++] = (byte) (0xc0 + (convert(ch)[0] & 0x1f));
            buffer[offset++] = (byte) (0x80 + (convert(ch)[1] & 0x3f));

//            if (ch < 0x80)
//                buffer[offset++] = (byte) (ch);
//            else if (ch < 0x800) {
//                buffer[offset++] = (byte) (0xc0 + ((ch >> 6) & 0x1f));
//                buffer[offset++] = (byte) (0x80 + (ch & 0x3f));
//            }
//            else {
//                buffer[offset++] = (byte) (0xe0 + ((ch >> 12) & 0xf));
//                buffer[offset++] = (byte) (0x80 + ((ch >> 6) & 0x3f));
//                buffer[offset++] = (byte) (0x80 + (ch & 0x3f));
//            }
        }

        setSuperFieldValue("_offset", offset);
    }

    @Override
    public void printString(char[] v, int strOffset, int length) throws IOException {
        int offset = (int) getSuperFieldValue("_offset");
        byte[] buffer = (byte[]) getSuperFieldValue("_buffer");

        for (int i = 0; i < length; i++) {
            if (SIZE <= offset + 16) {
                setSuperFieldValue("_offset", offset);
                flushBuffer();
                offset = (int) getSuperFieldValue("_offset");
            }

            char ch = v[i + strOffset];

            // 2 bytes UTF-8
            buffer[offset++] = (byte) (0xc0 + (convert(ch)[0] & 0x1f));
            buffer[offset++] = (byte) (0x80 + (convert(ch)[1] & 0x3f));

//            if (ch < 0x80)
//                buffer[offset++] = (byte) (ch);
//            else if (ch < 0x800) {
//                buffer[offset++] = (byte) (0xc0 + ((ch >> 6) & 0x1f));
//                buffer[offset++] = (byte) (0x80 + (ch & 0x3f));
//            }
//            else {
//                buffer[offset++] = (byte) (0xe0 + ((ch >> 12) & 0xf));
//                buffer[offset++] = (byte) (0x80 + ((ch >> 6) & 0x3f));
//                buffer[offset++] = (byte) (0x80 + (ch & 0x3f));
//            }
        }

        setSuperFieldValue("_offset", offset);
    }

    public int[] convert(int i) {
        int b1 = ((i >> 6) & 0b11111) | 0b11000000;
        int b2 = (i & 0b111111) | 0b10000000;
        return new int[]{ b1, b2 };
    }

    public Object getSuperFieldValue(String name) {
        try {
            Field f = this.getClass().getSuperclass().getDeclaredField(name);
            f.setAccessible(true);
            return f.get(this);
        } catch (Exception e) {
            return null;
        }
    }

    public void setSuperFieldValue(String name, Object val) {
        try {
            Field f = this.getClass().getSuperclass().getDeclaredField(name);
            f.setAccessible(true);
            f.set(this, val);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
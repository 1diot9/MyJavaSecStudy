package tools;

import com.caucho.hessian.io.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HessianTools {
    public static byte[] hessian2ToStringSer(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(67);
        Hessian2Output hessian2Output = new Hessian2Output(baos);

        SerializerFactory serializerFactory = new SerializerFactory();
        serializerFactory.setAllowNonSerializable(true);
        hessian2Output.setSerializerFactory(serializerFactory);

        hessian2Output.writeObject(obj);
        hessian2Output.close();
        return baos.toByteArray();
    }


    public static byte[] hessian2Ser2bytes(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Hessian2Output hessian2Output = new Hessian2Output(baos);

        SerializerFactory serializerFactory = new SerializerFactory();
        serializerFactory.setAllowNonSerializable(true);
        hessian2Output.setSerializerFactory(serializerFactory);

        hessian2Output.writeObject(obj);
        hessian2Output.close();
        return baos.toByteArray();
    }

    public static Object hessian2Deser(byte[] bytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        Hessian2Input hessian2Input = new Hessian2Input(bais);
        Object o = hessian2Input.readObject();
        hessian2Input.close();
        return o;
    }

    public static byte[] hessianSer2bytes(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HessianOutput hessianOutput = new HessianOutput(baos);

        SerializerFactory serializerFactory = new SerializerFactory();
        serializerFactory.setAllowNonSerializable(true);
        hessianOutput.setSerializerFactory(serializerFactory);

        hessianOutput.writeObject(obj);
        hessianOutput.close();
        return baos.toByteArray();
    }

    public static Object hessianDeser(byte[] bytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        HessianInput hessianInput = new HessianInput(bais);
        Object o = hessianInput.readObject();
        hessianInput.close();
        return o;
    }
}

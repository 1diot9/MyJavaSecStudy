package tools;

import com.caucho.hessian.io.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HessianTools {
    /**
     * 反序列化时触发 toString
     * @param obj
     * @return
     * @throws IOException
     */
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


    public static byte[] hessianSer2bytes(Object obj, String version) throws IOException {
        if (version.equals("1")){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            HessianOutput hessianOutput = new HessianOutput(baos);

            SerializerFactory serializerFactory = new SerializerFactory();
            serializerFactory.setAllowNonSerializable(true);
            hessianOutput.setSerializerFactory(serializerFactory);

            hessianOutput.writeObject(obj);
            hessianOutput.close();
            return baos.toByteArray();
        }else if (version.equals("2")){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Hessian2Output hessian2Output = new Hessian2Output(baos);

            SerializerFactory serializerFactory = hessian2Output.getSerializerFactory();
            serializerFactory.setAllowNonSerializable(true);
            hessian2Output.setSerializerFactory(serializerFactory);

            hessian2Output.writeObject(obj);
            hessian2Output.flush();
            return baos.toByteArray();
        }
        return null;
    }

    public static Object hessianDeser(byte[] bytes, String version) throws IOException {
        if (version.equals("1")){
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            HessianInput hessianInput = new HessianInput(bais);
            Object o = hessianInput.readObject();
            hessianInput.close();
            return o;
        }else if (version.equals("2")){
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            Hessian2Input hessian2Input = new Hessian2Input(bais);
            Object o = hessian2Input.readObject();
            hessian2Input.close();
            return o;
        }
        return null;
    }
}

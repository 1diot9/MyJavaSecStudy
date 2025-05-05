import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Util {

    public static String getDefaultTestCmd(){
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Mac")) {
            return "open /System/Applications/Calculator.app";
        }
        return "calc";
    }

    public static void writeObj2File(Object obj,String path) throws IOException {
        System.out.println("set obj to "+ path);
        FileOutputStream fileOut = new FileOutputStream(path);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(obj);
        out.close();
        fileOut.close();
    }

    public static Object readObj4File(String path) throws IOException, ClassNotFoundException {
        System.out.println("get obj for "+ path);
        FileInputStream fileInput = new FileInputStream(path);
        ObjectInputStream input = new ObjectInputStream(fileInput);
        return input.readObject();
    }

    public static void runGadgets(Object obj)throws Exception{
        byte[] ser = serialize(obj);
        deserialize(ser);
    }

    public static byte[] serialize(final Object obj) throws IOException {
        System.out.println("serialize obj:  "+ obj.getClass().getName());
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(obj);
        return out.toByteArray();
    }

    public static Object deserialize(byte[] ser) throws IOException, ClassNotFoundException {
        System.out.println("deserialize obj");
        final ByteArrayInputStream in = new ByteArrayInputStream(ser);
        final ObjectInputStream objIn = new ObjectInputStream(in);
        return objIn.readObject();
    }


    /**
     * 字节数组转16进制
     * @param bytes 需要转换的byte数组
     * @return  转换后的Hex字符串
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if(hex.length() < 2){
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * hex字符串转byte数组
     * @param hex 待转换的Hex字符串
     * @return  转换后的byte数组结果
     */
    public static byte[] hexToByteArray(String hex){
        int hexlen = hex.length();
        byte[] result;
        if (hexlen % 2 == 1){
            hexlen++;
            result = new byte[(hexlen/2)];
            hex="0"+hex;
        }else {
            result = new byte[(hexlen/2)];
        }
        int j=0;
        for (int i = 0; i < hexlen; i+=2){
            result[j]=(byte)Integer.parseInt(hex.substring(i,i+2),16);
            j++;
        }
        return result;
    }

    public static String join(String[] strings, String sep, String prefix, String suffix) {
        final StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String s : strings) {
            if (! first) sb.append(sep);
            if (prefix != null) sb.append(prefix);
            sb.append(s);
            if (suffix != null) sb.append(suffix);
            first = false;
        }
        return sb.toString();
    }

    public static String join(String[] strings, String sep) {
        final StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String s : strings) {
            if (! first) sb.append(sep);
            sb.append(s);
            first = false;
        }
        return sb.toString();
    }

    public static HashMap createSilenceHashMap(Object key1, Object value1, Object key2, Object value2) throws Exception {
        return createSilenceHashMap(new Object[]{key1,key2},new Object[]{value1,value2});
    }

    public static HashMap createSilenceHashMap(Object[] keys, Object[]values) throws Exception {
        HashMap map = new HashMap();
        Reflections.setFieldValue(map, "size", keys.length);
        Class nodeC;
        try {
            nodeC = Class.forName("java.util.HashMap$Node");
        }
        catch ( ClassNotFoundException e ) {
            nodeC = Class.forName("java.util.HashMap$Entry");
        }
        Constructor nodeCons = nodeC.getDeclaredConstructor(int.class, Object.class, Object.class, nodeC);
        nodeCons.setAccessible(true);
        Object tbl = Array.newInstance(nodeC, keys.length);
        for(int i=0;i<keys.length;i++){
            Object key   = keys[i];
            Object value = values[i];
            Array.set(tbl, i, nodeCons.newInstance(0, key, value, null));
        }

        Reflections.setFieldValue(map, "table", tbl);
        return map;
    }


    public static String array2String(Object[] objects,String lineHeader){
        StringBuilder builder = new StringBuilder();
        if(objects==null || objects.length==0){
            return "";
        }
        builder.append(lineHeader + ":[");
        for(Object o:objects){
            builder.append(o.toString() + ",");
        }
        return builder.substring(0,builder.length()-1) + "]";
    }


    // 寻找字符串中字符出现的所有位置
    public static int[] findAllPositionsOfChar(String str, char ch) {
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ch) {
                positions.add(i);
            }
        }
        int[] positionArray = new int[positions.size()];
        for(int i=0;i<positions.size();i++){
            positionArray[i] = positions.get(i);
        }
        return positionArray;
    }

}


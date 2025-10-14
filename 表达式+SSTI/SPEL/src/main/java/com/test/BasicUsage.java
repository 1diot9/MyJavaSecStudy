package com.test;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class BasicUsage {
    public String name = "baka";

    public static void main(String[] args) {
        usage1();
        usage2();
    }

    public static void usage1(){
        ExpressionParser parser = new SpelExpressionParser();//创建解析器
        Expression exp = parser.parseExpression("'Hello World'.concat('!')");//解析表达式
        System.out.println( exp.getValue() );//取值，Hello World！
    }

    public static void usage2(){
        BasicUsage user = new BasicUsage();
        StandardEvaluationContext context=new StandardEvaluationContext();
        context.setVariable("user",user);//通过StandardEvaluationContext注册自定义变量
        SpelExpressionParser parser = new SpelExpressionParser();//创建解析器
        Expression expression = parser.parseExpression("#user.name");//解析表达式
        System.out.println( expression.getValue(context).toString() );//取值,输出何止
    }
}

package com.spring.controller;

import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class TestController {
    /*由于tomcat对GET请求中的| {} 等特殊字符存在限制(RFC 3986)，所以使用POST方法传递参数*/
    @ResponseBody
    @PostMapping(value = "/index")
    public String index(String string) throws IOException {
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        Expression expression = spelExpressionParser.parseExpression(string);
        String out = (String) expression.getValue();
        out = out.concat(" get");
        return out;
    }

    @ResponseBody
    @PostMapping(value = "/index2")
    public String index2(String string) throws IOException {
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        TemplateParserContext templateParserContext = new TemplateParserContext();
        /*使用模板解析，传参时需要加上#{}*/
        Expression expression = spelExpressionParser.parseExpression(string, templateParserContext);
        Integer out = (Integer) expression.getValue();
        return Integer.toString(out);
    }
}


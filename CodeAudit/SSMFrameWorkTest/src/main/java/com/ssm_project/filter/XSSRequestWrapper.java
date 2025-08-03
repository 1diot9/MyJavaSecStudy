package com.ssm_project.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.text.StringEscapeUtils;

public class XSSRequestWrapper extends HttpServletRequestWrapper {


    public XSSRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getHeader(String name) {
        return  StringEscapeUtils.escapeHtml3(super.getHeader(name))  ;
    }

    @Override
    public String getQueryString() {
        return StringEscapeUtils.escapeHtml3(super.getQueryString());
    }

    @Override
    public String getParameter(String name) {
        return StringEscapeUtils.escapeHtml3(super.getParameter(name));
    }
/**
 * 这里将过滤规则写在如下两个方法中，这样的用意是在程序任何地方调用
 * request.getParameterValues()方法来获取前端传入的参数是本质上都会
 * 调用这里自定义的XSSRequestWrapper里重写的这么一个getParameterValues()
 * 所以每一次获取参数都会将从request对象中获取的数据进行一次过滤和转义
 * */
    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if(values != null){
            int length = values.length;
            String[] escapseValues = new String[length];
            for ( int i=0; i<length;i++){
                escapseValues[i] = htmlEncode(values[i]);
            }
            return escapseValues;
        }
        return super.getParameterValues(name);
    }

    private static String htmlEncode(String source){
        if (source == null){
            return "";
        }
        String html="";
        StringBuffer  buffer = new StringBuffer();
        for(int i =0;i<source.length();i++){
            char c = source.charAt(i);
            switch (c) {
                case '<':
                    buffer.append("&lt;");
                    break;
                case '>':
                    buffer.append("&gt;");
                    break;
                case '&':
                    buffer.append("&amp;");
                    break;
                case '"':
                    buffer.append("&quot;");
                    break;
                case 10:
                case 13:
                    break;
                default:
                    buffer.append(c);
            }

        }
        html = buffer.toString();
        return html;
    }

}

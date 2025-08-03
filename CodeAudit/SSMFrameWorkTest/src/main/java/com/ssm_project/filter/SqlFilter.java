package com.ssm_project.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * 过滤器是servlet提供的一种技术，它允许用户在参数未进入业务程序之前对参数进行拦截和过滤
 * 如何全局查找过滤器，很简单，去web.xml中查看，因为Filter想要生效就需要在web.xml中进行如下所示的配置
 *   <filter>
 *     <filter-name>SQLEscape</filter-name>
 *     <filter-class>com.ssm_project.filter.SqlFilter</filter-class>
 *   </filter>
 *   <filter-mapping>
 *     <filter-name>SQLEscape</filter-name>
 *     <url-pattern>/*</url-pattern>
 *     <dispatcher>REQUEST</dispatcher>
 *   </filter-mapping>
 * 不过在serlvet 3.0版本 servlet也开始支持通过注解来进行配置，所以部分项目中可能就看不到web.xml了
 * 碰到这种情况，全局搜索Filter这个关键字就可以，因为用户如果需要自定义Sql注入或者XSS等的过滤器那么就一定需要
 * 实现Filter接口
 * */
public class SqlFilter implements Filter {
/**
 * init和destroy方法都不用太在意关键是doFilter方法
 * */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }
/**
 * 该方法是主要用来进行过滤的方法，方法内的三个参数为 request对象，response对象以及FilterChain对象
 *
 * */
    @Override
    public void doFilter(ServletRequest args0, ServletResponse args1,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req=(HttpServletRequest)args0;
        HttpServletResponse res=(HttpServletResponse)args1;
        //获得所有请求参数名
        Enumeration params = req.getParameterNames();
        String sql = "";
        while (params.hasMoreElements()) {
            //得到参数名
            String name = params.nextElement().toString();
            //System.out.println("name===========================" + name + "--");
            //得到参数对应值
            String[] value = req.getParameterValues(name);
            for (int i = 0; i < value.length; i++) {
                sql = sql + value[i];
            }
        }
        //System.out.println("============================SQL"+sql);
        //有sql关键字，跳转到error.html
        if (sqlValidate(sql)) {
            throw new IOException("您发送请求中的参数中含有非法字符");
            //String ip = req.getRemoteAddr();
        } else {
            chain.doFilter(args0,args1);
        }
    }

    @Override
    public void destroy() {

    }

    /**
     * 这里就是过滤规则，这个就不细说了
     * */
    protected static boolean sqlValidate(String str) {
        str = str.toLowerCase();//统一转为小写
        String badStr = "'|and|exec|execute|insert|select|delete|update|count|drop|*|%|chr|mid|master|truncate|" +
                "char|declare|sitename|net user|xp_cmdshell|;|or|-|+|,|like'|and|exec|execute|insert|create|drop|" +
                "table|from|grant|use|group_concat|column_name|" +
                "information_schema.columns|table_schema|union|where|select|delete|update|order|by|count|*|" +
                "chr|mid|master|truncate|char|declare|or|;|-|--|+|,|like|//|/|%|#";//过滤掉的sql关键字，可以手动添加
        String[] badStrs = badStr.split("\\|");
        for (int i = 0; i < badStrs.length; i++) {
            if (str.indexOf(badStrs[i]) >= 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 最后总结在审计sql注入的时候如何去查找和判断Filter
     * 可以通过web.xml查看Filter配置，也可以通过全局搜索Filter关键词来查找Filter
     * 剩下的就是观察过滤规则了，该例子中使用的过滤器就规则就非常严格
     * */
}

package com.ssm_project.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class XssFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        /**
         * 这里可以看到XSS的过滤不同于之前的SQL注入过滤，之前的SQL注入的选择是一旦匹配到关键词就终止执行直接返回报错页面
         * 而这里的逻辑则是匹配特殊符号，然后对特殊符号进行转义，再将转义后的内容存储进数据库从而实现避免xss攻击的目的
         * 但是request对象并没有提供直接修改其前端传入参数的方法，所以只能通过RequestWrapper来达到修改参数的目的
         * 所以在挖掘漏洞的过程中如果碰到这种情况就跟进RequestWrapper深入查看
         * */

        chain.doFilter(new XSSRequestWrapper((HttpServletRequest) request),response);


    }

    @Override
    public void destroy() {

    }
}
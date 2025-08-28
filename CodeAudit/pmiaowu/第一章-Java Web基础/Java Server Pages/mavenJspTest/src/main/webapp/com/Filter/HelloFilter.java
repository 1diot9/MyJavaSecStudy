package com.Filter;

import javax.servlet.*;
import java.io.IOException;

public class HelloFilter implements Filter {
    @Override
    public void init(FilterConfig config) throws ServletException {
        // 获取初始化参数
        String origin = config.getInitParameter("origin");
        String testName = config.getInitParameter("testName");
        // 输出初始化参数
        System.out.println("HelloFilter类起源: " + origin);
        System.out.println("HelloFilter类testName值: " + testName);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 输出站点名称
        response.setCharacterEncoding("GBK");
        response.getWriter().println("站点网址：http://pmiaowu.phpoop.com");

        // 允许访问目标资源,简称 放行
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("HelloFilter类,被摧毁了");
    }
}
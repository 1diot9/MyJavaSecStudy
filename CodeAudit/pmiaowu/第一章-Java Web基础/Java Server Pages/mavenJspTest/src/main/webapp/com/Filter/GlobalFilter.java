// 在这目录: src->main->webapp->com->Filter
// 创建一个文件: GlobalFilter.java
// 创建完毕以后GlobalFilter.java需要实现了Filter接口,并实现了3个方法
// 这3个方法的作用已经在注释中写清楚了

package com.Filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import java.io.IOException;

@WebFilter(
        filterName = "/GlobalFilter",
        urlPatterns = "/*",
        initParams = {
                @WebInitParam(name = "origin", value = "@WebFilter"),
                @WebInitParam(name = "testName", value = "testValue")
        },
        dispatcherTypes = {
                DispatcherType.REQUEST,
                DispatcherType.INCLUDE,
                DispatcherType.FORWARD,
                DispatcherType.ERROR,
                DispatcherType.ASYNC})
public class GlobalFilter implements Filter {
    /**
     * web 应用程序启动时
     * web 服务器将创建Filter 的实例对象，并调用其init方法，读取web.xml配置，完成对象的初始化功能
     * 从而为后续的用户请求作好拦截的准备工作（filter对象只会创建一次，init方法也只会执行一次）
     * 开发人员通过init方法的参数，可获得代表当前filter配置信息的FilterConfig对象
     *
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig config) throws ServletException {
        // 获取初始化参数
        String origin = config.getInitParameter("origin");
        String testName = config.getInitParameter("testName");
        // 输出初始化参数
        System.out.println("GlobalFilter类起源: " + origin);
        System.out.println("GlobalFilter类testName值: " + testName);
    }

    /**
     * 该方法完成实际的过滤操作，当客户端请求方法与过滤器设置匹配的URL时，Servlet容器将先调用过滤器的doFilter方法
     *
     * @param request  当前请求
     * @param response 当前响应
     * @param chain    用于访问后续过滤器
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 输出站点名称
        response.setCharacterEncoding("GBK");
        response.getWriter().println("我是全局过滤器: GlobalFilter类");

        // 允许访问目标资源,简称 放行
        chain.doFilter(request, response);
    }

    /**
     * Filter容器在销毁过滤器实例前调用该方法
     */
    @Override
    public void destroy() {
        System.out.println("GlobalFilter类,被摧毁了");
    }
}
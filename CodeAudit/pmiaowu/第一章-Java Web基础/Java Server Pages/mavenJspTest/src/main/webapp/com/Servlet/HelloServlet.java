// 在这目录: src->main->webapp->com->Servlet
// 创建一个文件: HelloFilter.java

package com.Servlet;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet实现类HelloServlet
 * 注册一个注解,这样方便我们不通过 web.xml 也可以在web中访问该类
 */
@WebServlet("/HelloServlet")
public class HelloServlet extends HttpServlet {
    public HelloServlet() {
        super();
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 使用 GBK 设置中文正常显示
        response.setCharacterEncoding("GBK");
        response.getWriter().write("HelloServlet类GET方法被调用");
    }

    /**
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 使用 GBK 设置中文正常显示
        response.setCharacterEncoding("GBK");
        response.getWriter().write("HelloServlet类POST方法被调用");
    }
}
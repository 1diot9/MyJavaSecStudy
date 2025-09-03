package com.Servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/TestServlet2")
public class TestServlet2 extends HttpServlet {
    public TestServlet2() {
        super();
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        response.getWriter().write("id: " + id);
    }
}




// 接着打开运行:http://127.0.0.1:8081/mavenJspTest_war/TestServlet2?id=1
// 返回结果
// id: 1
// request.getParameter 获取请求参数的值
// response.getWriter().write 输出内容到响应中
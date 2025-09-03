package com.Servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/SessionTest2")
public class SessionTest2 extends HttpServlet {
    /**
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 设置Session
        HttpSession session = request.getSession();
        String sessionKey = "ss";
        String sessionValue = "sv";
        session.setAttribute(sessionKey, sessionValue);

        // 读取Session
        response.getWriter().println(" ");
        response.getWriter().println("isNew? : " + session.isNew());
        response.getWriter().println("sessionID: " + session.getId());
        response.getWriter().println("sessionKey: " + sessionKey);
        response.getWriter().println("sessionValue: " + session.getAttribute(sessionKey));
        response.getWriter().println(" ");

        response.getWriter().println("okk");
    }
}




// 执行该文件
// 例如: http://127.0.0.1:8081/mavenJspTest_war/SessionTest2
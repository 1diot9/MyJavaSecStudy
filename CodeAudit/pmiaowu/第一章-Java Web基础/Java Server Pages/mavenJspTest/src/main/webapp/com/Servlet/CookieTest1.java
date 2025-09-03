package com.Servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/CookieTest1")
public class CookieTest1 extends HttpServlet {
    /**
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 设置Cookie
        Cookie cookie = new Cookie("ck", "cv");
        cookie.setMaxAge(10 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);

        // 读取Cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                response.getWriter().println(" ");
                response.getWriter().println("cookieKey: " + c.getName());
                response.getWriter().println("cookieValue: " + c.getValue());
                response.getWriter().println(" ");
            }
        }

        response.getWriter().println("okk");
    }
}




// 执行该文件
// 例如: http://127.0.0.1:8081/mavenJspTest_war/CookieTest1
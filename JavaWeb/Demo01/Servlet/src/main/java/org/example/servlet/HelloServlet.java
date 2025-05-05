package org.example.servlet;

import java.io.*;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "HelloServlet", urlPatterns = {"/hello", "/hello/t"})
public class HelloServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        String name = req.getParameter("name");
        String[] ages = req.getParameterValues("age");
        String hello = req.getRealPath("hello");

        writer.println("hello " + name);
        writer.println("age " + Arrays.toString(ages));
        writer.println(hello);
        writer.flush();
        writer.close();
    }
}
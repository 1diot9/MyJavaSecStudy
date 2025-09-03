// 文件名字: C3P0Demo1.java
package com.Servlet;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/C3P0Demo1")
public class C3P0Demo1 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String DRIVER_NAME = "com.mysql.jdbc.Driver";
        String URL = "jdbc:mysql://127.0.0.1:3306/mysql";
        String USER_NAME = "root";
        String PASSWORD = "123456";

        Connection conn = null;
        PreparedStatement ps = null;
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass(DRIVER_NAME);
            dataSource.setJdbcUrl(URL);
            dataSource.setUser(USER_NAME);
            dataSource.setPassword(PASSWORD);
            conn = dataSource.getConnection();

            String sql = "SELECT * FROM `user`";
            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            response.getWriter().println(" ");
            response.getWriter().println("C3P0Demo1.java");

            while (rs.next()) {
                response.getWriter().println(" ");
                response.getWriter().println("用户名:" + rs.getString("User") + "   " + "密码:" + rs.getString("authentication_string"));
            }
        } catch (PropertyVetoException | SQLException e) {
            e.printStackTrace();
        } finally {
            //释放stmt
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}




// 执行该文件
// 例如: http://127.0.0.1:8081/mavenJspTest_war/C3P0Demo1
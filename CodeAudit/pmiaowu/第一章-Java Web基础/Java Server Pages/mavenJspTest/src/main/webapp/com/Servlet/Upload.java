// 路径: ./项目/src/main/webapp/com/Servlet/Upload.java
package com.Servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@WebServlet("/upload")
public class Upload extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 判断是否是多段数据(只有是多段数据才是文件上传),多段数据时,返回true
        if (!ServletFileUpload.isMultipartContent(request)) {
            response.getWriter().println("no");
            return;
        }

        // 文件保存地址
        String realPath = request.getSession().getServletContext().getRealPath("/uploads");

        // 解决⽂件夹存放⽂件数量限制,按⽇期存放
        String datePath = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String uploadPath = realPath + "/" + datePath;
        File floder = new File(uploadPath);
        if (!floder.exists()) {
            floder.mkdirs();
        }

        // 创建FileItemFactory工厂实现类
        FileItemFactory factory = new DiskFileItemFactory();

        // 创建用于解析数据的工具类ServletFileUpload类
        ServletFileUpload servletFileUpload = new ServletFileUpload(factory);

        // 解析上传的数据,得到每一个表单项FileItem
        try {
            List<FileItem> list = servletFileUpload.parseRequest(request);

            // 判断每个表单项是普通类型还是上传的文件
            for (FileItem item : list) {
                if (!item.isFormField()) {
                    // 获取⽂件的扩展名,如txt
                    String extendName = item.getName().substring(item.getName().lastIndexOf(".") + 1).trim().toLowerCase();

                    // 新的⽂件名字
                    String newName = UUID.randomUUID().toString() + "." + extendName;

                    // 保存文件在本地
                    item.write(new File(uploadPath + "/" + newName));

                    // 输出上传路径
                    response.getWriter().println("uploadName:" + item.getFieldName());
                    response.getWriter().println("uploadPath:" + uploadPath + "/" + newName);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.getWriter().println("nonono");
        return;
    }
}
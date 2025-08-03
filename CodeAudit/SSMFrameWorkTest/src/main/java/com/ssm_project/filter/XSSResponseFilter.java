package com.ssm_project.filter;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.ssm_project.filter.XSSResponseWrapper;


/**
 * 返回值输出过滤器，这里用来加密返回值
 *
 * @Title: ResponseFilter
 * @Description:
 * @author kokJuis
 * @date 上午9:52:42
 */
public class XSSResponseFilter implements Filter
{

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException
    {
        XSSResponseWrapper wrapperResponse = new XSSResponseWrapper((HttpServletResponse)response);//转换成代理类
        // 这里只拦截返回，直接让请求过去，如果在请求前有处理，可以在这里处理
        filterChain.doFilter(request, wrapperResponse);
        byte[] content = wrapperResponse.getContent();//获取返回值
        //判断是否有值
        if (content.length > 0)
        {

            String str = new String(content, "UTF-8");
            System.out.println("返回值:" + str);
            String ciphertext = null;

            try
            {
                //......根据需要处理返回值
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
//            //把返回值输出到客户端
//            ServletOutputStream out = response.getOutputStream();
//            //我测试了需要加上这行代码,才能对 response的内容 修改生效，
//            //好像是需要保持长度一致，不然请求就一直会处于等待，希望对后来的小伙伴有点帮助
//            out.setContentLength(ciphertext.getBytes().length);
//            out.write(ciphertext.getBytes());
//            out.flush();
        }

    }

    @Override
    public void init(FilterConfig arg0)
            throws ServletException
    {

    }

    @Override
    public void destroy()
    {

    }

}

// 第一步
// 配置一个全局拦截器
// 路径: ./项目/src/main/com/test/interceptor/TestAllWebRequestInterceptor.java
package test.interceptor;

import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

public class TestAllWebRequestInterceptor implements WebRequestInterceptor {
    @Override
    public void preHandle(WebRequest request) throws Exception {
        request.setAttribute("preHandle-testkey1", "testvaue1", WebRequest.SCOPE_REQUEST);
        System.out.println("TestAllWebRequestInterceptor类的preHandle方法在在控制器的处理请求方法前执行");
    }

    @Override
    public void postHandle(WebRequest request, ModelMap model) throws Exception {
        request.setAttribute("postHandle-testkey", "testvaue2", WebRequest.SCOPE_REQUEST);
        System.out.println("TestAllWebRequestInterceptor类的postHandle方法在控制器的处理请求方法调用之后,解析视图之前执行");
    }

    @Override
    public void afterCompletion(WebRequest request, Exception ex) throws Exception {
        request.setAttribute("afterCompletion-testkey", "testvaue3", WebRequest.SCOPE_REQUEST);
        System.out.println("TestAllWebRequestInterceptor类的afterCompletion方法在控制器的处理请求方法执行完成后执行,即视图渲染结束之后执行");
    }
}
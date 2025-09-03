// 第一步
// 配置一个全局拦截器
// 路径: ./项目/src/main/com/test/interceptor/TestAllHandlerInterceptor.java
package test.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestAllHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("TestAllHandlerInterceptor类的preHandle方法在在控制器的处理请求方法前执行");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("TestAllHandlerInterceptor类的postHandle方法在控制器的处理请求方法调用之后,解析视图之前执行");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("TestAllHandlerInterceptor类的afterCompletion方法在控制器的处理请求方法执行完成后执行,即视图渲染结束之后执行");
    }
}
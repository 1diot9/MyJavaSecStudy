package Tools;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.support.RequestContextUtils;


//各种拿到WebApplicationContext的方法
public class WaysToFindContext {
    public static WebApplicationContext way01(){
        WebApplicationContext context = (WebApplicationContext) RequestContextHolder.currentRequestAttributes().getAttribute("org.springframework.web.servlet.DispatcherServlet.CONTEXT", 0);
        return context;
    }

    public static WebApplicationContext way02() throws IllegalAccessException, ClassNotFoundException, NoSuchFieldException {
        // 1. 反射 org.springframework.context.support.LiveBeansView 类 applicationContexts 属性
        java.lang.reflect.Field filed = Class.forName("org.springframework.context.support.LiveBeansView").getDeclaredField("applicationContexts");
        // 2. 属性被 private 修饰，所以 setAccessible true
        filed.setAccessible(true);
        // 3. 获取一个 ApplicationContext 实例
        org.springframework.web.context.WebApplicationContext context =(org.springframework.web.context.WebApplicationContext) ((java.util.LinkedHashSet)filed.get(null)).iterator().next();
        return context;
    }


}

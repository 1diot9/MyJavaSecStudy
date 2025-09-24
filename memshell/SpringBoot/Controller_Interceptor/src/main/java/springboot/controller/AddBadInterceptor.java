package springboot.controller;

import bad_interceptor.BadInterceptor_within;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;

//动态添加Interceptor
@Controller
public class AddBadInterceptor {
    @RequestMapping("/addBadInterceptor")
    public void shell() throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        //获得context
        WebApplicationContext context = Tools.WaysToFindContext.way01();
        //获取 adaptedInterceptors 属性值
        org.springframework.web.servlet.handler.AbstractHandlerMapping abstractHandlerMapping = (org.springframework.web.servlet.handler.AbstractHandlerMapping) context.getBean("requestMappingHandlerMapping");
        java.lang.reflect.Field field = org.springframework.web.servlet.handler.AbstractHandlerMapping.class.getDeclaredField("adaptedInterceptors");
        field.setAccessible(true);
        java.util.ArrayList<Object> adaptedInterceptors = (java.util.ArrayList<Object>) field.get(abstractHandlerMapping);
        BadInterceptor_within aaa = new BadInterceptor_within("aaa");
        adaptedInterceptors.add(aaa);
        System.out.println("BadInterceptor_within has been added");
    }
}

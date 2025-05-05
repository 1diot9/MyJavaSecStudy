import java.lang.reflect.InvocationHandler;

public class Proxy {

    public static Object makeGadget(InvocationHandler handler, Class... classes) throws Exception {
        return java.lang.reflect.Proxy.newProxyInstance(Proxy.class.getClassLoader(), classes, handler);
    }
}

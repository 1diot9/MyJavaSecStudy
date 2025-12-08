package tools;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

// 动态代理，实现对象接口转化
public class InvocationHandlerImpl implements InvocationHandler, Serializable {
    private Object object;

    public InvocationHandlerImpl(Object obj) {
        this.object = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}

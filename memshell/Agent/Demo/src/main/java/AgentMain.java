import java.lang.instrument.Instrumentation;

public class AgentMain {
    public static void agentmain(String agentArgs, Instrumentation ins) {
        System.out.println("\nagentmain start!!!\n");
        ins.addTransformer(new DefineTransformer(),true);
    }
}
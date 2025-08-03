package tmp;

import com.ruoyi.quartz.util.JobInvokeUtil;

import java.util.List;

public class JobInvokeTest {
    public static void main(String[] args) {
        String invoke = "javax.naming.InitialContext.lookup('ldap://127.0.0.1:50389/c78d33', 123, \"test\", new Object[]{})";
        List<Object[]> methodParams = JobInvokeUtil.getMethodParams(invoke);
        for (Object[] methodParam : methodParams) {
            for (Object param : methodParam) {
                System.out.print(param + " ~ ");
            }
            System.out.println();
        }
    }
}

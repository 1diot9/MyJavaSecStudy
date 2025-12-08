package RMI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    public static void main(String[] args) throws Exception {
        serverAttackClientWithJRMP();
    }

    // 从registry获取的stub指向恶意skel，通过DGC JRMP，实现server打client
    public static void serverAttackClientWithJRMP() throws Exception{
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 13999);
        registry.lookup("951d14");
    }

    public static void rmiDeser() throws Exception{
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 50388);
        registry.lookup("c4e578");
    }
}

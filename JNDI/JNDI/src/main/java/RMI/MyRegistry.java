package RMI;

import remoteObj.HelloImpl;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class MyRegistry {
    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        java.rmi.registry.Registry registry = LocateRegistry.createRegistry(1099);
        registry.bind("hello", new HelloImpl());
    }
}

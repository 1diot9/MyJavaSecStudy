package remoteObj;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HelloImpl extends UnicastRemoteObject implements Hello {
    public HelloImpl() throws RemoteException {
    }

    @Override
    public String hello(String name) throws RemoteException {
        return "hello " + name;
    }
}

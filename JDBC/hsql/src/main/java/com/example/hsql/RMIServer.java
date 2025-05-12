package com.example.hsql;

import com.sun.jndi.rmi.registry.ReferenceWrapper;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.Reference;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

//随手复习一下RMI而已
public class RMIServer {
    public static void main(String[] args) throws RemoteException, NamingException {
        Registry registry = LocateRegistry.createRegistry(1098);
        Reference reference = new Reference("Calc", "Calc", "http://127.0.0.1:7777/");
        ReferenceWrapper referenceWrapper = new ReferenceWrapper(reference);
        registry.rebind("Calc", referenceWrapper);
        System.out.println("rmi server started at 1098 port");

        InitialContext initialContext = new InitialContext();
        initialContext.lookup("rmi://127.0.0.1:1098/Calc");
    }
}

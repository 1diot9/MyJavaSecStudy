import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JndiClient {
    public static void main(String[] args) throws NamingException {
        String rmiHost = "rmi://127.0.0.1:1099/Calc123";
        String ldapHost = "ldap://127.0.0.1:1389/anything";
        InitialContext initialContext = new InitialContext();
        initialContext.lookup(ldapHost);
    }
}

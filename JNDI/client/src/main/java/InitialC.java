import javax.naming.InitialContext;
import javax.naming.NamingException;

public class InitialC {
    public static void main(String[] args) throws NamingException {
        InitialContext initialContext = new InitialContext();
        initialContext.lookup("rmi://127.0.0.1:1099/Object");
    }
}

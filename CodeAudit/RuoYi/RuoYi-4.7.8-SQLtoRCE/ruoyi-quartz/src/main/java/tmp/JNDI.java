package tmp;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JNDI {
    public static void main(String[] args) throws NamingException {
        InitialContext initialContext = new InitialContext();
        initialContext.lookup("l'dap://127.0.0.1:50389/c78d33");
    }
}

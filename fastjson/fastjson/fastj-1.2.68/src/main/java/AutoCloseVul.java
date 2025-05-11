import java.io.IOException;

public class AutoCloseVul implements AutoCloseable {
    public AutoCloseVul(String cmd) {
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {

    }
}

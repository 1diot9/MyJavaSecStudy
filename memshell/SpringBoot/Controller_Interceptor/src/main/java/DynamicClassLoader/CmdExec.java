package DynamicClassLoader;

import java.io.IOException;

public class CmdExec {
    public static void main(String[] args) throws IOException {
        Runtime.getRuntime().exec("calc");
    }
}

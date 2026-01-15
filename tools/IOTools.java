package exp.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class IOTools {
    public static byte[] readFile(String fileName) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(fileName));
        return bytes;
    }
}

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Requests {

    public static void post(String host, byte[] data) throws Exception {
        // 目标 URL
        URL url = new URL(host);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // 配置请求
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/octet-stream");

        // 从 ser1.bin 读取序列化数据

//        File file = new File("./ser1.bin");
//        byte[] data = new byte[(int) file.length()];
//        FileInputStream fis = new FileInputStream(file);
//        fis.read(data);

        // 写入请求体
        try (OutputStream os = conn.getOutputStream()) {
            os.write(data);
            os.flush();
        }

        // 读取响应
        try (InputStream is = conn.getInputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }

        conn.disconnect();
    }
}

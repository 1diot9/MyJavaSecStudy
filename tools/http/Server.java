package tools.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class Server {
    public static void main(String[] args) throws Exception {
        start(8078, "exp");
    }

    public static void start(int port, String route) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", port), 0);
        String path = "";
        if (route != null && !route.equals("")) {
            path = route;
        }
        server.createContext("/" + path, new ExploitHandler());

        server.start();
        System.out.println("[+] Exploit Server started on port " + port + "route " + path);
    }

    static class ExploitHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("\n[*] Received request from victim!");

            String requestMethod = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            System.out.println("    Method: " + requestMethod);
            System.out.println("    Path: " + path);

            // 解析参数
            Map<String, String> params = new HashMap<>();

            // GET参数从query中获取
            String query = exchange.getRequestURI().getQuery();
            if (query != null && !query.isEmpty()) {
                parseParams(query, params);
            }

            // POST参数从body中获取
            if ("POST".equalsIgnoreCase(requestMethod)) {
                InputStream is = exchange.getRequestBody();
                byte[] requestBody = readAllBytes(is);
                is.close();

                if (requestBody.length > 0) {
                    String body = new String(requestBody);
                    System.out.println("    Body: " + body);
                    parseParams(body, params);
                }
            }

            System.out.println("    Params: " + params);

            // 根据参数决定响应
//            byte[] responseBytes = handleParams(params);
            byte[] responseBytes = "ok123".getBytes();

            // 发送响应
            exchange.sendResponseHeaders(200, responseBytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(responseBytes);
            os.close();

            System.out.println("[+] Response sent!");
        }

        /**
         * 根据参数处理并返回响应内容
         */
        private byte[] handleParams(Map<String, String> params) {
            // 这里可以根据参数自定义响应逻辑
            String action = params.get("action");
            if (action != null) {
                switch (action) {
                    case "test":
                        return "Test response".getBytes();
                    case "file":
                        String file = params.get("file");
                        if (file != null) {
                            try {
                                return tools.IOTools.readFile(file);
                            } catch (IOException e) {
                                return ("File not found: " + e.getMessage()).getBytes();
                            }
                        }
                        return "Missing file parameter".getBytes();
                    default:
                        return ("Unknown action: " + action).getBytes();
                }
            }
            return "OK".getBytes();
        }

        /**
         * 解析URL编码的参数
         */
        private void parseParams(String query, Map<String, String> params) {
            if (query == null || query.isEmpty()) {
                return;
            }
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] kv = pair.split("=", 2);
                if (kv.length == 2) {
                    try {
                        String key = URLDecoder.decode(kv[0], "UTF-8");
                        String value = URLDecoder.decode(kv[1], "UTF-8");
                        params.put(key, value);
                    } catch (Exception e) {
                        // 忽略解码错误
                    }
                }
            }
        }

        /**
         * 读取InputStream的所有字节 (Java 8兼容)
         */
        private static byte[] readAllBytes(InputStream is) throws IOException {
            java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
            byte[] data = new byte[4096];
            int nRead;
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            return buffer.toByteArray();
        }
    }

}

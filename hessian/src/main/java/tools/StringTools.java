package tools;

public class StringTools {
    /**
     * 将byte[]转换为\\x前缀的十六进制字符串格式
     * 例如: byte[]{0xca, 0xfe} -> "\\xca\\xfe"
     * @param bytes 字节数组
     * @return 格式化后的十六进制字符串
     */
    public static String bytesToHexString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(bytes.length * 4);
        for (byte b : bytes) {
            sb.append("\\x");
            String hex = Integer.toHexString(b & 0xff);
            if (hex.length() == 1) {
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}

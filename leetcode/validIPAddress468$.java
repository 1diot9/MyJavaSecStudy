import java.util.regex.Pattern;

public class validIPAddress468$ {
    public static void main(String[] args) {
        validIPAddress("172.16.254.1");
    }

    public static String validIPAddress(String queryIP) {
        String ipv4 = "25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9]";
        String ipv6 = "[0-9a-fA-F]{1,4}";
        Pattern patternV4 = Pattern.compile(ipv4);
        Pattern patternV6 = Pattern.compile(ipv6);

        String[] split = new String[]{};
        if (queryIP.contains(".") && queryIP.lastIndexOf(".") != queryIP.length() - 1){
            split = queryIP.split("\\.");
        }else if (queryIP.contains(":") && queryIP.lastIndexOf(":") != queryIP.length() - 1){
            split = queryIP.split(":");
        }


        if (split.length == 4){
            int flag = 1;
            for (int i=0;i<4;i++){
                if (!patternV4.matcher(split[i]).matches()){
                    flag = 0;
                    break;
                }
            }
            if (flag == 1){
                return "IPv4";
            }
        }

        if (split.length == 8){
            int flag = 1;
            for (int i=0;i<8;i++){
                if (!patternV6.matcher(split[i]).matches()){
                    flag = 0;
                    break;
                }
            }
            if (flag == 1){
                return "IPv6";
            }
        }

        return "Neither";
    }

    public static String validIPAddress1(String queryIP) {
        String ipv4 = "^(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])\\." +
                "(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])\\." +
                "(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])\\." +
                "(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])$";
        String ipv6 = "^([0-9a-fA-F]{1,4}):" +
                "([0-9a-fA-F]{1,4}):" +
                "([0-9a-fA-F]{1,4}):" +
                "([0-9a-fA-F]{1,4}):" +
                "([0-9a-fA-F]{1,4}):" +
                "([0-9a-fA-F]{1,4}):" +
                "([0-9a-fA-F]{1,4}):" +
                "([0-9a-fA-F]{1,4})$";
        Pattern pattern1 = Pattern.compile(ipv4);
        Pattern pattern2 = Pattern.compile(ipv6);

        if (pattern1.matcher(queryIP).matches()){
            return "IPv4";
        }

        if (pattern2.matcher(queryIP).matches()){
            return "IPv6";
        }

        return "Neither";
    }
}

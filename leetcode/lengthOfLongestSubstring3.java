public class lengthOfLongestSubstring3 {
    public static void main(String[] args) {
        lengthOfLongestSubstring1("abcabcbb");
    }

    // 超时了
    public static int lengthOfLongestSubstring(String s) {
        int maxLength = 52 + 10 + 32 + 1;
        int length = s.length();

        if (length < maxLength){
            for (int window=length;window>0;--window){
                for (int i=0;i<=length-window;i++){
                    String substring = s.substring(i, i + window);
                    int flag = 1;
                    int[] array = new int[127];
                    for (int j=0;j<window;j++){
                        int ascii = substring.charAt(j);
                        if (array[ascii] == 0){
                            array[ascii] = 1;
                        }else {
                            flag = 0;
                            break;
                        }
                    }
                    if (flag == 1){
                        return window;
                    }
                }
            }
        }

        for (int window=maxLength;window>0;--window){
            for (int i=0;i<=length-window;i++){
                String substring = s.substring(i, i + window);
                int flag = 1;
                int[] array = new int[127];
                for (int j=0;j<window;j++){
                    int ascii = substring.charAt(j);
                    if (array[ascii] == 0){
                        array[ascii] = 1;
                    }else {
                        flag = 0;
                        break;
                    }
                }
                if (flag == 1){
                    return window;
                }
            }
        }

        return 0;
    }

    // 正确方法
    public static int lengthOfLongestSubstring1(String s) {
        int maxLength = 52 + 10 + 32 + 1;
        int strLength = s.length();
        // 最大窗口上限
        int length = Math.min(maxLength, s.length());
        int maxWindow = 0;

        // 有长度，maxWindow 至少为1
        if (length != 0){
            maxWindow = 1;
        }

        for (int window=2;window<=length;window++){
            for (int i=0;i<=strLength-window;i++){
                String substring = s.substring(i, i + window);
                int flag = 1;
                // 数组用于标记是否出现重复字符
                int[] array = new int[128];
                for (int j=0;j<window;j++){
                    int ascii = substring.charAt(j);
                    if (array[ascii] == 0){
                        array[ascii] = 1;
                    }else {
                        flag = 0;
                        break;
                    }
                }
                if (flag == 1){
                    maxWindow = window;
                    break;
                }
            }
            if (window - maxWindow >= 1){
                break;
            }
        }

        return maxWindow;
    }
}

public class longestCommonPrefix14 {
    public static void main(String[] args) {
        String s = "111";

    }

    public String longestCommonPrefix(String[] strs) {
        int min = Integer.MAX_VALUE;
        int minIdx = 0;
        for (int i=0;i<strs.length;i++){
            String str = strs[i];
            int len = str.length();
            if (len < min){
                min = len;
                minIdx = i;
            }
        }

        String minStr = strs[minIdx];
        StringBuilder longest = new StringBuilder();
        int flag = 1;
        for (int i=0;i<minStr.length();i++){
            char c = minStr.charAt(i);
            for (String str : strs){
                if (str.charAt(i) != c){
                    flag = 0;
                    break;
                }
            }
            if (flag == 0){
                break;
            }
            longest.append(c);
        }

        return longest.toString();
    }

    public String longestCommonPrefix2(String[] strs) {
        String prefix = strs[0];
        while (!prefix.isEmpty()){
            int flag = 1;
            for (String str : strs){
                if (!str.startsWith(prefix)){
                    prefix = prefix.substring(0, prefix.length()-1);
                    flag = 0;
                    break;
                }
            }
            if (flag == 1){
                return prefix;
            }
        }
        return "";
    }
}

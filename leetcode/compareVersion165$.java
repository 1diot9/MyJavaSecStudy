public class compareVersion165$ {
    public static void main(String[] args) {

    }


    public int compareVersion(String version1, String version2) {
        String[] v1 = version1.split("\\.");
        String[] v2 = version2.split("\\.");

        int len1 = v1.length;
        int len2 = v2.length;
        int len = Math.min(len1, len2);

        for (int i=0;i<len;i++){
            int s1 = Integer.parseInt(v1[i]);
            int s2 = Integer.parseInt(v2[i]);
            if (s1 < s2){
                return -1;
            }else if (s1 > s2){
                return 1;
            }
        }
        if (len1 == len2){
            return 0;
        }

        if (len1 > len){
            for (int i=len;i<len1;i++){
                int v = Integer.parseInt(v1[i]);
                if (v > 0){
                    return 1;
                }
            }
        }else if (len2 > len){
            for (int i=len;i<len2;i++){
                int v = Integer.parseInt(v2[i]);
                if (v > 0){
                    return -1;
                }
            }
        }else {
            return 0;
        }
        return 0;
    }
}

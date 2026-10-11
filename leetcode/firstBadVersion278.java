import java.nio.file.Paths;

public class firstBadVersion278 {
    public static void main(String[] args) {
        firstBadVersion(2126753390);
    }

    public static int firstBadVersion(int n) {
        int start = 1;
        int end = n;

        while (start < end){
            // 防止 int 溢出
            int mid = start + (end-start) / 2;
            boolean badVersion = isBadVersion(mid);
            if (badVersion){
                end = mid;
            }else {
                start = mid + 1;
            }
        }
        return start;
    }

    public static boolean isBadVersion(int version){
        int badVersion = 1702766719;
        return version >= badVersion;
    }
}

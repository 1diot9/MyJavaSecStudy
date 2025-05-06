import java.util.Timer;

//斐波那契数列
public class Practice03 {
    public static void main(String[] args) {
        feb(440);
    }

    public static void feb(int n) {
        int a = 1;
        int b = 1;
        int c;
        long start = System.nanoTime();
        //运算一次，能得到第三位，所以运算n-2次，得到第n位
        for (int i = 0; i < n-2; i++) {
            c = b;
            //得到最新的a和b
            b = a + b;
            a = c;
        }
        System.out.println(b);
        long end = System.nanoTime();
        System.out.println(end-start + "ns");

    }
}

package practice;

//青蛙跳台阶
public class Practice03 {
    public static void main(String[] args) {
        int i = countWays(39);
        System.out.println(i);
    }

    public static int countWays(int n) {
        if (n == 0) return 1;
        if (n == 1) return 1;
        if (n == 2) return 2;

        int prePer = 1;
        int pre = 2;
        int current = 0;

        for (int i = 2; i < n; i++) {
            current = prePer + pre;
            prePer = pre;
            pre = current;
        }
        return current;

//        return countWays(n - 1) + countWays(n - 2);
    }
}

//九九乘法表
public class Practice02 {
    public static void main(String[] args) {
        math();
    }

    public static void math(){
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < i + 1; j++) {
                System.out.print(String.format("%d * %d = %2d  ", j, i, i * j));
            }
            System.out.println();
        }
    }
}

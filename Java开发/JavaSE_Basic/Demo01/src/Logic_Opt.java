public class Logic_Opt {
    public static void main(String[] args) {
        int a = 0;
        if (0 > 3 && ++a > 4){  //短路，++a不会执行
        }
        System.out.println(a);

        if (0 < 3 || ++a > 4){  //短路

        }
        System.out.println(a);

        int b = a > 1 ? 5 : 6;  //true:false
        System.out.println(b);
    }
}

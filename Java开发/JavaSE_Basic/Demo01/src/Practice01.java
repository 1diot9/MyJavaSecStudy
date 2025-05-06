//水仙花数
//“水仙花数（Narcissistic number）也被称为超完全数字不变数（pluperfect digital invariant, PPDI）、自恋数、自幂数、阿姆斯壮数或阿姆斯特朗数（Armstrong number），
// 水仙花数是指**一个 3 位数，它的每个位上的数字的 3次幂之和等于它本身。**例如：1^3 + 5^3+ 3^3 = 153。”
//现在请你设计一个Java程序，打印出所有1000以内的水仙花数。
public class Practice01 {
    public static void main(String[] args) {
        test();
    }

    public static void test() {
        int num01;  //个位
        int num02;
        int num03;
        for (int i = 100; i < 1000; i++) {
            num01 = i % 10;
            num02 = i / 10 % 10;
            num03 = i / 100 % 10;
            if (i == (int) (Math.pow(num01, 3) + Math.pow(num02, 3) + Math.pow(num03, 3))){
                System.out.print(i + ", ");
            }
        }
    }
}

public class Bit_Opt {
    public static void main(String[] args) {
        int a = 4;
        int b = 6;
        System.out.println(a & b);  //按2进制位与运算
        System.out.println(a | b);  //按位或
        System.out.println(a ^ b);  //异或    不是乘方
        System.out.println(~a);     //按位取反，得到的是补码
        a = 1;
        System.out.println(a << 2); //0001 左移 0100
        a = 4;
        System.out.println(a >> 2);

        a = -4;
        System.out.println(a >> 1); //11111100  左移 11111110 前面用1补位

        byte c = -1;
        System.out.println(c >>> 1);    //不考虑符号位右移，高位用0补位，自动转为int运算

        //没有无符号左移

    }
}

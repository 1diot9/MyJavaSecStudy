/**
 * @author 1diot9
 * @since 2025.5.5
 */
public class Main {


    public static void main(String[] args){
        System.out.println("Hello world!");
        final int z = 777;  //常量，不可修改，只能第一次赋值
        byte a = 127;
        short b = 11;
        int c;
        long d = 1233333333333333L;  //最后要加L，不然默认是int
        c = b;
        c = 1_000_000; //_无影响
        System.out.println(0xA);
        System.out.println(013);    //8进制

        c = 2147483647;
        c = c + 1;
        System.out.println(c);  //会溢出

        float f = 0.1f; //有小数点默认是double
        System.out.println(f);
        double g = 19.3;


        char str = 65;
        System.out.println(str);

        String s = "sss,你好";    //String是对象类型，而不是基本类型
        System.out.println(s);

        System.out.println(8/3);    //省略小数
        System.out.println(8.0/3);


        //===========================运算

        int aa = 10;
        int bb = 20;
        bb = (aa = 3) * (-aa + 8);
        System.out.println(bb);     //15

        aa = 8;
        bb = -aa++ + ++aa;  //先a++，aa = 8，再-aa，aa = 9，再++aa，aa = 10
        System.out.println(bb);


    }
}
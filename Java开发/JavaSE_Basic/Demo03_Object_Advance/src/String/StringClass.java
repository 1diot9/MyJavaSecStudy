package String;

public class StringClass {
    public static void main(String[] args) {
        String str1 = "hello world";
        String str2 = "hello world";

        System.out.println(str1.equals(str2));  //比较字符串内容用equals，而不是==。因为通过new String()创建的字符串==时返回false


        System.out.println(str1.substring(0, 3));

        String[] s = str1.split(" ");

        char[] charArray = str2.toCharArray();  //char String 转化
        String s1 = new String(charArray);

    }
}

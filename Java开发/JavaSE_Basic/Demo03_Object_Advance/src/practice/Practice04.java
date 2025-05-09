package practice;

import java.util.Arrays;
import java.util.Scanner;

public class Practice04 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入字符串：");
        String s = scanner.nextLine();
        boolean reverse = reverse(s);
        System.out.println(reverse);
        scanner.close();
    }

    public static boolean reverse(String s) {
        char[] charArray = s.toCharArray();
        int length = charArray.length;
        int i = 0;
        int j = length - 1;
        do {
            char front = charArray[i];
            char back = charArray[j];
            if (front != back) return false;
            i++;
            j--;
        }while (i <= j);
        return true;
    }
}

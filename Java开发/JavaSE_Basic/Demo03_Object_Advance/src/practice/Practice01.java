package practice;

import java.util.Arrays;

//冒泡排序
public class Practice01 {
    public static void main(String[] args) {
        int[] arr = new int[]{3, 5, 7, 2, 9, 0, 6, 1, 8, 4};
        int swap = 0;
        do {
            swap = 0;
            int tmp;
            for (int i = 0; i < arr.length - 1; i++) {
                if (arr[i] > arr[i + 1]) {
                    tmp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = tmp;
                    swap++;
                }
            }

        }while (swap != 0);
        System.out.println(Arrays.toString(arr));
    }
}

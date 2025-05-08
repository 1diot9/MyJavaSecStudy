package tools;

import java.util.Arrays;
import java.util.Random;

public class RandomClass {
    public static void main(String[] args) {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        int[] arr = new int[10];
        for (int i = 0; i < 10; i++) {
            arr[i] = random.nextInt(100);
        }
        System.out.println(Arrays.toString(arr));
        Arrays.sort(arr);
        System.out.println(Arrays.toString(arr));

        int[] ints = Arrays.copyOf(arr, 10);

        int[] ints1 = Arrays.copyOfRange(ints, 0, 10);

        arr = new int[]{1,2,3,4,5,6,7,8,9,10};
        System.out.println(Arrays.binarySearch(arr, 4));



    }
}

package practice;

//二分法
public class Practice02 {
    public static void main(String[] args) {
        int[] arr = {1, 3, 4, 6, 7, 8, 10, 11, 13};
        int target = 7;
        int low = 0;
        int high = 0;
        do {
            int mid = (arr[low] + arr[high]) / 2;
            if (arr[mid] == target) {
                System.out.println(String.format("arr[%d] = %d", mid, target));
                break;
            }else if (arr[mid] > target) {
                high = mid - 1;
            }else {
                low = mid + 1;
            }
        }while (true);
    }
}

public class smallestIndex3550 {
    public int smallestIndex(int[] nums) {
        for (int i=0;i<nums.length;i++){
            int num = nums[i];
            int sum = 0;
            while (num != 0){
                int n = num % 10;
                sum += n;
                num = num / 10;
            }
            if (sum == i){
                return i;
            }
        }
        return -1;
    }
}

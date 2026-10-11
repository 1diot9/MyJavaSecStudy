import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class SystemIn {
    public static void main(String[] args) throws IOException {
        t5();

    }

    public static void t1() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        int sum = 0;
        while (stringTokenizer.hasMoreTokens()) {
            int num = Integer.parseInt(stringTokenizer.nextToken());
            sum += num;
        }
        System.out.println(sum);
    }

    public static void t2() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        int[] array = new int[n];
        int i = 0;
        while (st.hasMoreTokens()){
            int a = Integer.parseInt(st.nextToken());
            array[i] = a;
            i++;
        }

        int sum = Arrays.stream(array).sum();
        System.out.println(sum);
    }

    public static void t3() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int lines = Integer.parseInt(st.nextToken());
        int columns = Integer.parseInt(st.nextToken());

        int[][] array = new int[lines][columns];
        int sum = 0;
        for (int i=0;i<lines;i++){
            st = new StringTokenizer(br.readLine());
            for (int j=0;j<columns;j++){
                array[i][j] = Integer.parseInt(st.nextToken());
                sum += array[i][j];
            }
        }
        System.out.println(sum);
        System.out.println(Arrays.deepToString(array));
    }

    public static void t4() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());

        int[] sum = new int[n];
        for (int i=0;i<n;i++){
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            sum[i] = a+b;
        }

        for (int i=0;i<n;i++){
            System.out.println(sum[i]);
        }
    }

    public static void t5() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());

        int sum = 0;
        for (int i=0;i<n;i++){
            st = new StringTokenizer(br.readLine());
            while (st.hasMoreTokens()){
                sum += Integer.parseInt(st.nextToken());
            }
        }
        System.out.println(sum);
    }
}

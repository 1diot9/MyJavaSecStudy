public class Array_t {
    public static void main(String[] args) {
        int[] a = new int[10];
        for (int i = 0; i < a.length; i++) {
            a[i] = i;
        }
        for (int i : a){
            System.out.print(i + " ");
        }

        String[][] b = new String[5][3];
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[i].length; j++) {
                b[i][j] = String.format("(%d,%d)", i, j);
            }
        }

        System.out.println();

        for (String[] i : b){
            for (String j : i){
                System.out.print(j + " ");
            }
            System.out.println();
        }

    }
}

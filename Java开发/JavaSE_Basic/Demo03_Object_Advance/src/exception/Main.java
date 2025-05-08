package exception;

public class Main {
    public static void main(String[] args) {
        try {
            Object obj = null;
            obj.toString();
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }


        try {
            System.out.println("try 1");
            int[] arr = new int[1];
            arr[1] = 100;    //这里发生的是数组越界异常，它是运行时异常的子类
            System.out.println("try 2");
        } catch (RuntimeException e){  //使用运行时异常同样可以捕获到
            e.printStackTrace();
            System.out.println("捕获到异常");
        }finally {
            System.out.println("finally");
        }
    }
}

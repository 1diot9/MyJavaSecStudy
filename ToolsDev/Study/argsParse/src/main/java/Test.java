public class Test {
    public static void main(String[] args) {
        System.out.println(args.length);
        int loop = 0;
        for (String arg : args){
            if (arg.equals("-h")){
                System.out.println("-h get helps");
            }else if (arg.equals("-o")){
                String para = args[loop + 1];
                System.out.println("your output path is " + para);
            }
            loop++;
        }
    }
}

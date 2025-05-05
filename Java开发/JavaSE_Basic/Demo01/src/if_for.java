public class if_for {
    public static void main(String[] args) {
        study_if(11);
        study_switch("web");
        study_for(5);
    }

    public static void study_if(int a){
        if (a > 10){
            System.out.println("a > 10");
        } else if (a == 10) {
            System.out.println("a == 10");
        }else {
            System.out.println("a < 10");
        }
    }

    public static void study_switch(String ctf){
        switch (ctf){
            case "web":
                System.out.println("Study web is fun");
                break;
            case "pwn":
                System.out.println("pwn is difficult");
                break;
            case "misc":
                System.out.println("misc");
                break;
            default:
                System.out.println("reverse or mobile");
        }
    }

    public static void study_for(int i){
        for (int j = 0; j < i; j++){
            if (j == 1) continue;
            if (j == i - 1) break;
            System.out.println("j: " + j);
        }
    }

    public static void study_while(int k){
        while (k > 0){
            k--;
            System.out.println("k: " + k);
            if (k == 3) break;
        }

        do {
            System.out.println("k");
            k--;
        }while (k > -2);
    }
}

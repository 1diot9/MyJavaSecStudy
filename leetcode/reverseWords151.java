public class reverseWords151{
    public static void main(String[] args) {

    }

    public String reverseWords(String s){
        s = s.trim();
        String[] split = s.split("\\s+");
        int len = split.length;
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<len-1;i++){
            sb.append(split[len - i - 1]).append(" ");
        }
        sb.append(split[0]);
        System.out.println(sb.toString());
        return sb.toString();
    }
}

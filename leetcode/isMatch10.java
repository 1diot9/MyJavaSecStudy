import java.util.regex.Pattern;

public class isMatch10 {
    public boolean isMatch(String s, String p) {
        Pattern pattern = Pattern.compile(p);
        return pattern.matcher(s).matches();
    }
}

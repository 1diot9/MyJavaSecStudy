import com.alibaba.fastjson.annotation.JSONType;

import java.io.IOException;

@JSONType
public class CalcJType {
    public CalcJType() {
        try {
            Runtime.getRuntime().exec("calc");
            System.out.println("CalcJType no-args block");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static {
        try {
            Runtime.getRuntime().exec("notepad");
            System.out.println("CalcJType static block");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

package tools.sinkClass;

import java.io.Serializable;

public class ToStringClass implements Serializable {
    public String toString(){
        System.out.println("toString pwned");
        return "toString pwned";
    }

    public int hashCode(){
        System.out.println("hashCode pwned");
        return 0;
    }
}

package pojo;

import java.io.Serializable;

public class ToStringClass implements Serializable {
    @Override
    public String toString() {
        System.out.println("toString");
        return "pojo.ToStringClass{}";
    }
}

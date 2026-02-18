package toString;

public class GetterClass implements java.io.Serializable {
    public String name;

    public String getName() {
        System.out.println("getName");
        return name;
    }
}

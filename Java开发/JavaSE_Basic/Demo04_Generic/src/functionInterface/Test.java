package functionInterface;

public interface Test {
    void test(String s);

    default Test ttt(){
        return (String s) ->  {test(s);};
    }
}

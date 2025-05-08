package InnerClass;


//成员内部类
public class Test01 {
    private final String name;

    public Test01(String name) {
        this.name = name;
    }

    public class Inner {
        private String name;

        public Inner(String name) {
            this.name = name;
        }

        public void test(String name) {
            System.out.println("name: " + name);
            System.out.println("this name: " + this.name);
            System.out.println("Test01.this.name: " + Test01.this.name);
        }
    }
}

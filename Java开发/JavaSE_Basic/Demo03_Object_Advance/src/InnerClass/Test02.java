package InnerClass;


public class Test02 {


    //无法访问外部类的非静态变量，因为非静态变量属于对象，而静态类属于类
    public static class Inner{
        public void test(){
            System.out.println("静态内部类 test");
        }
    }

    public void test(){
        class Inner{
            public void test(){
                System.out.println("局部内部类 test");
            }
        }
        Inner inner = new Inner();
        inner.test();
    }
}

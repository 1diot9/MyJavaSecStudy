package InnerClass;

public class Main {
    public static void main(String[] args) {
        Test01 test01 = new Test01("1diot9");
        Test01.Inner inner = test01.new Inner("baka");    //内部类依附于具体对象
        inner.test("param");

        Test02.Inner inner1 = new Test02.Inner();
        inner1.test();
        Test02 test02 = new Test02();
        test02.test();

        AbstractClass anything = new AbstractClass() {
            @Override
            public void test() {
                System.out.println("匿名内部类：" + name);
            }
        };
        anything.test();

        //lambda表达式，仅用于接口，且接口内只有一个抽象方法。
        //格式(参数类型 参数名称) -> {方法 返回值}
        Interface01 study = (String name) -> {
            System.out.println("I study " + name);
            return name;
        };
        study.study("Java");

        //只有一个return，可以省略return和花括号
        Test test = (String a) -> "test lambda " + a;
        System.out.println(test.test("abc"));

        //方法引用，直接用静态方法作为接口实现
        Interface02 interface02 = Integer::sum;
        interface02.sum(2, 3);
    }
}

public class Person {
    String name;
    int age;
    static String info;     //定义静态变量

    //无参构造默认在编译时自带，不信可以去看看字节码
    public Person() {
    }

    //如果没有手动创建无参构造，有参构造会把默认的无参构造挤掉
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    {
        System.out.println("我是代码块");   //代码块中的内容会在对象创建时仅执行一次
    }

    //返回值有 void int String 等
    void hello() {
        System.out.printf("hello my name is %s", name);
    }

    //方法作用域里的变量不会影响到外面。即这里的形参x，y是经过重新赋值的，不是对同一个对象的引用。
    void swap(int x, int y) {
        int temp = x;
        x = y;
        y = temp;
    }

    int sum(int a, int b) {
        return a + b;
    }

    //重载，要保证形参发生变化
    double sum(double a, double b) {
        return a + b;
    }

    public void setName(String name) {
        //使用this代表引用的是成员变量name，而不是最近作用域，即形参里的name
        this.name = name;
    }
}

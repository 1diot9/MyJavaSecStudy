import java.lang.reflect.Field;

public class Main {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        //a b 都是对同一个对象的引用
        Person a = new Person();
        Person b = a;
        System.out.println(a == b);

        //这里a b 就是对不同对象的引用了
        a = new Person();
        b = new Person();
        System.out.println(a == b);

        Person p1 = new Person("cc", 1);
        p1.name = "1diot9";
        p1.hello();

        int x = 2;
        int y = 3;

        p1.swap(x,y);

        Person a1 = new Person();
        Person a2 = new Person();

        a1.info = "hello aaa";
        System.out.println(a2.info);    //静态变量是所有对象共享的
        Field info = a1.getClass().getDeclaredField("info");
        info.setAccessible(true);
        info.set(a1, "bbb");
        System.out.println(a2.info);    //反射修改a1的info，a2的也会跟着变


    }
}

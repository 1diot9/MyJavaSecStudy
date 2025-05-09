package genericClass;

public class Main {

    public static void main(String[] args) {
        TestClass<Integer> good = new TestClass<Integer>("1diot9", 20, 70);
        Integer grade1 = good.getGrade();

        //?代表可以赋值为任意确定的泛型类
        TestClass<? extends Number> bad = new TestClass<>("1diot9", 20, 66);
        Number grade = bad.getGrade();
    }

}


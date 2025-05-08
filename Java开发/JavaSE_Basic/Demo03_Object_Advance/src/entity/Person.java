package entity;

public class Person {
    private String name;
    private int age;

    //可变长参数，实际上是数组
    public void test(String... strs){
        for (String str : strs){
            System.out.println(str);
        }
    }

    public void test01(String[] strings){
        for (String str : strings){
            System.out.println(str);
        }
    }
}

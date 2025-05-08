package entity;

public interface Study {
    public void study(); //接口中只能定义访问权限为public抽象方法，其中public和abstract关键字可以省略

    //默认方法，实现类中不强制
    default void test(){
        System.out.println("default");
    }
}

//封装
public class Test01 {
    private String name;
    private int age;
    private static Test01 instance;

    private Test01(){
    }

    //单例模式：全局只能使用一个对象
    public static Test01 getInstance(){
        if(instance == null){
            instance = new Test01();
        }
        return instance;
    }

    public String getName() {
        return name;
    }

    //具体逻辑交给类内部自身实现，外部只需调用setName即可，实现封装
    public void setName(String name) {
        System.out.println("Log: set name" + name);
        this.name = name;
    }
}

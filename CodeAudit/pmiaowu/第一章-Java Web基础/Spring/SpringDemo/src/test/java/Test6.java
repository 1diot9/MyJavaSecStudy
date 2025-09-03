// 第四步
// 创建一个测试类Test6进行测试
// 目录: ./SpringDemo/src/test/java/
// 文件名: Test6.java
import com.test.HelloWorld2;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Test6 {
    public static void main(String[] args) {
        // scope="singleton"作用域两次输出结果一致,说明是同一个bean
        // scope="prototype"两次输出结果不一致,说明不是同一个bean
        ApplicationContext context = new FileSystemXmlApplicationContext("src/main/resources/applicationContext.xml");

        HelloWorld2 obj1 = (HelloWorld2) context.getBean("helloWorldTest2");
        System.out.println(obj1.hashCode());

        HelloWorld2 obj2 = (HelloWorld2) context.getBean("helloWorldTest2");
        System.out.println(obj2.hashCode());
    }
}




// 运行结果
//1795960102
//        1027591600
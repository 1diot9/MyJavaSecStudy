// 第四步
// 创建一个测试类Test5进行测试
// 目录: ./SpringDemo/src/test/java/
// 文件名: Test5.java
import com.test.HelloWorld;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Test5 {
    public static void main(String[] args) {
        // scope="singleton"作用域两次输出结果一致,说明是同一个bean
        // scope="prototype"两次输出结果不一致,说明不是同一个bean
        ApplicationContext context = new FileSystemXmlApplicationContext("src/main/resources/applicationContext.xml");

        HelloWorld obj1 = (HelloWorld) context.getBean("helloWorldTest");
        System.out.println(obj1.hashCode());

        HelloWorld obj2 = (HelloWorld) context.getBean("helloWorldTest");
        System.out.println(obj2.hashCode());
    }
}




// 运行结果
//923219673
//1604125387
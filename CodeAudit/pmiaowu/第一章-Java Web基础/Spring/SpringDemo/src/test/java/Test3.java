// 创建一个测试类Test3进行测试
// 目录: ./SpringDemo/src/test/java/
// 文件名: Test3.java
import com.test.HelloWorld;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Test3 {
    public static void main(String[] args) {
        ApplicationContext context = new FileSystemXmlApplicationContext("src/main/resources/applicationContext.xml");
        HelloWorld obj = (HelloWorld) context.getBean("helloWorldTest");
        obj.getMessage();
    }
}




// 运行结果
//Your Message : Hello World!
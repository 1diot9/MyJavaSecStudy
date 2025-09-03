// 创建一个测试类Test4进行测试
// 目录: ./SpringDemo/src/test/java/
// 文件名: Test4.java
import com.test.HelloWorld;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test4 {
    public static void main(String[] args) {
        // 获取Spring的上下文对象
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        HelloWorld obj = (HelloWorld) context.getBean("helloWorldTest");
        obj.getMessage();
    }
}





// 运行结果
//Your Message : Hello World!
// 第二步
// 添加一个AnnotationConfigApplicationContext类进行扫描
// 目录: ./SpringDemo/src/test/java/
// 文件名: Test27.java
import com.test.AppConfig;
import com.test.HelloWorld;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test27 {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        HelloWorld obj = (HelloWorld) context.getBean("helloWorldTest");
        obj.getMessage();
    }
}





// 运行结果
//Your Message: Hello World!
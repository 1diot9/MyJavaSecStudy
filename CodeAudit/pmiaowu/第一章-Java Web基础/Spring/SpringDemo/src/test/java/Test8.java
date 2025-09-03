// 第三步
// 创建一个测试类Test8进行测试
// 目录: ./SpringDemo/src/test/java/
// 文件名: Test8.java
import com.test.HelloWorld3;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test8 {
    public static void main(String[] args) {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        HelloWorld3 obj = (HelloWorld3) context.getBean("helloWorldTest3");
        obj.getMessage();

        // 注册一个关闭钩子,关闭Spring IOC容器
        // 用来触发bean标签的destroy-method属性
        context.registerShutdownHook();
    }
}
// 第三步
// 创建一个测试类AutomaticScanTest进行测试
// 目录: ./SpringDemo/src/test/java/
// 文件名: AutomaticScanTest.java
import com.test.Bean自动扫描.UserServlet;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 测试使用注解进行bean的管理
 */
public class AutomaticScanTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("automaticScanContextConfig.xml");
        UserServlet obj = (UserServlet) context.getBean("userServlet");
        obj.doGet();
    }
}




// 运行结果
//addUser方法执行了
//        delUser方法执行了
//updateUser方法执行了
//        selectUser方法执行了
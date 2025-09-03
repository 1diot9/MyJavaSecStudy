// 第四步
// 创建一个测试类Test进行测试
// 目录: ./SpringDemo/src/test/java/
// 文件名: Test.java
import com.test.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {
    public static void main(String[] args) {
        // 获取Spring的上下文对象
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        User user = (User) context.getBean("userTest");
        user.show();
    }
}





// 第二步
// 创建一个测试类lazyInitTest进行测试
// 目录: ./SpringDemo/src/test/java/
// 文件名: lazyInitTest.java
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class lazyInitTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("lazy-init-config.xml");
    }
}




// 运行结果
//UserInfo被初始化了
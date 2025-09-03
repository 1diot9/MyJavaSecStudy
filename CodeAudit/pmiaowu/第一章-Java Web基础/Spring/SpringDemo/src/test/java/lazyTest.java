// 第二步
// 创建一个测试类lazyTest进行测试
// 目录: ./SpringDemo/src/test/java/
// 文件名: lazyTest.java
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class lazyTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("lazy-config.xml");
    }
}




// 运行结果
//UserInfo2被初始化了
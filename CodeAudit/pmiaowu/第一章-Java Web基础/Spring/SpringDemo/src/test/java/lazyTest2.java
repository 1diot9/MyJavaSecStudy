// 第二步
// 创建一个测试类lazyTest2进行测试
// 目录: ./SpringDemo/src/test/java/
// 文件名: lazyTest2.java
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class lazyTest2 {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("lazy-config.xml");

        // 必须获取实例,不然不会进行实例化
        context.getBean("userInfo3Test");
    }
}




// 运行结果
//UserInfo3被初始化了
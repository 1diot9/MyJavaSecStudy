// 第二步
// 创建一个测试类lazyInitTest进行测试
// 目录: ./SpringDemo/src/test/java/
// 文件名: lazyInitTest2.java
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class lazyInitTest2 {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("lazy-init-config2.xml");

        // 必须获取实例,不然不会进行实例化
        context.getBean("userInfo");
    }
}




// 运行结果
//UserInfo被初始化了
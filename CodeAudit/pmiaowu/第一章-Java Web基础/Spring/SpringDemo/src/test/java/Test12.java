// 第三步
// 创建一个测试类Test12进行测试
// 目录: ./SpringDemo/src/test/java/
// 文件名: Test12.java
import com.test.Bean定义模板.HelloAmerica;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test12 {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext5.xml");
        HelloAmerica obj = (HelloAmerica) context.getBean("helloAmerica");
        obj.getMessage1();
        obj.getMessage2();
        obj.getMessage3();
    }
}






// 运行结果
//HelloAmerica-Message1: 你好,美国!
//HelloAmerica-Message2: 第二次说,你好!
//HelloAmerica-Message3: 第三次说,你好美国!
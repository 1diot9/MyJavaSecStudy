// 第四步
// 创建一个测试类Test11进行测试
// 目录: ./SpringDemo/src/test/java/
// 文件名: Test11.java
import com.test.Bean定义继承.Hello;
import com.test.Bean定义继承.HelloChina;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test11 {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext4.xml");

        Hello objA = (Hello) context.getBean("hello");
        objA.getMessage1();
        objA.getMessage2();

        System.out.println("-------------------------------");

        HelloChina objB = (HelloChina) context.getBean("helloChina");
        objB.getMessage1();
        objB.getMessage2();
        objB.getMessage3();
    }
}






// 运行结果
//Hello-Message1: 你好!
//Hello-Message2: 第二次说,你好!
//        -------------------------------
//HelloChina-Message1: 你好,中国!
//HelloChina-Message2: 第二次说,你好!
//HelloChina-Message3: 第三次说,你好中国!
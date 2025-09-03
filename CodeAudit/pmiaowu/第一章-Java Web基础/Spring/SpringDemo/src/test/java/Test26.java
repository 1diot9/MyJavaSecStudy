// 第五步
// 创建一个测试类Test26进行测试
// 目录: ./SpringDemo/src/test/java/
// 文件名: Test26.java
import com.test.自动装配.例三.Person3;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test26 {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext13.xml");
        Person3 obj = (Person3) context.getBean("personTest3");
        System.out.println(obj.speak());
    }
}


// 运行结果
//我叫马六,有只猫会喵呜呜呜呜叫,还有只狗会汪呜呜呜呜叫
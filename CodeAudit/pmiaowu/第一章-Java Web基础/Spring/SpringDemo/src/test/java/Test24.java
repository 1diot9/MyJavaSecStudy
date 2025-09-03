// 第五步
// 创建一个测试类Test24进行测试
// 目录: ./SpringDemo/src/test/java/
// 文件名: Test24.java
import com.test.自动装配.例一.Person;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test24 {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext11.xml");
        Person obj = (Person) context.getBean("personTest");
        System.out.println(obj.speak());
    }
}




// 运行结果
//我叫张三,有只猫会喵喵喵叫,还有只狗会汪汪汪叫
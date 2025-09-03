// 第五步
// 创建一个测试类Test25进行测试
// 目录: ./SpringDemo/src/test/java/
// 文件名: Test25.java
import com.test.自动装配.例二.Person2;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test25 {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext12.xml");
        Person2 obj = (Person2) context.getBean("personTest2");
        System.out.println(obj.speak());
    }
}




// 运行结果
//我叫李四,有只猫会喵喵喵喵喵喵叫,还有只狗会汪汪汪汪汪汪叫
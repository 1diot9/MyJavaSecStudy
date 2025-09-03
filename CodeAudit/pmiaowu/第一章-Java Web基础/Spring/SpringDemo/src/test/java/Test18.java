// 第三步
// 创建一个测试类Test18进行测试
// 目录: ./SpringDemo/src/test/java/
// 文件名: Test18.java
import com.test.依赖注入.Set方式注入.User3;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test18 {
    public static void main(String[] args) {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("applicationContext7.xml");
        User3 obj = (User3) context.getBean("userTest3");
        System.out.println(obj.getName());
        System.out.println(obj.getAge());
    }
}




// 运行结果
//陈七
//23
// 第五步
// 创建一个测试类Test30进行测试
// 目录: ./SpringDemo/src/test/java/
// 文件名: Test30.java
import com.test.基于注解的配置.AppContextConfig;
import com.test.基于注解的配置.注入数据的注解标签.Resource.Person;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test30 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppContextConfig.class);
        Person obj = (Person) context.getBean("resourceRersonTest");
        System.out.println(obj.speak());
    }
}




// 运行结果
//我叫王五,有只猫会喵喵喵叫,还有只狗会汪汪汪叫
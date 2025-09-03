// 第三步
// 使用AnnotationConfigApplicationContext将Bean注入到容器中
// 创建一个测试类Test28进行测试
// 目录: ./SpringDemo/src/test/java/
// 文件名: Test28.java
import com.test.基于注解的配置.AppContextConfig;
import com.test.基于注解的配置.创建对象的注解标签.Person;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test28 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppContextConfig.class);
        Person obj = (Person) context.getBean("personTest");
        System.out.println("我叫: " + obj.getName());
    }
}




// 运行结果
//我叫: 小王
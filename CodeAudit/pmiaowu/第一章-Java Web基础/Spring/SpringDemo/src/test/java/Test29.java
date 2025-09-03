// 第三步
// 创建一个测试类Test29进行测试
// 目录: ./SpringDemo/src/test/java/
// 文件名: Test29.java
import com.test.基于注解的配置.AppContextConfig;
import com.test.基于注解的配置.注入数据的注解标签.Value.Person2;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test29 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppContextConfig.class);
        Person2 obj = (Person2) context.getBean("person2Test");
        System.out.println("我叫: " + obj.getName());
        System.out.println("年龄: " + obj.getAge());
    }
}




// 运行结果
//我叫: 李四
//年龄: 18
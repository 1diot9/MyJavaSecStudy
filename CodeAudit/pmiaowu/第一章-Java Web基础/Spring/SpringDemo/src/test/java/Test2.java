// 创建一个测试类Test2进行测试
// 目录: ./SpringDemo/src/test/java/
// 文件名: Test2.java
import com.test.HelloWorld;
import org.springframework.core.io.ClassPathResource;
import org.springframework.beans.factory.xml.XmlBeanFactory;

public class Test2 {
    public static void main(String[] args) {
        XmlBeanFactory factory = new XmlBeanFactory(new ClassPathResource("applicationContext.xml"));
        HelloWorld obj = (HelloWorld) factory.getBean("helloWorldTest");
        obj.getMessage();
    }
}






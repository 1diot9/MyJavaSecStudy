// 第二步
// 此时修改为AnnotationConfigApplicationContext将Bean注入到容器中
// 创建一个测试类AutomaticScanTest2进行测试
// 目录: ./SpringDemo/src/test/java/
// 文件名: AutomaticScanTest2.java
import com.test.Bean自动扫描.AutomaticScanContextConfig;
import com.test.Bean自动扫描.UserServlet;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AutomaticScanTest2 {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AutomaticScanContextConfig.class);
        UserServlet obj = (UserServlet) context.getBean("userServlet");
        obj.doGet();
    }
}




// 运行结果
//addUser方法执行了
//        delUser方法执行了
//updateUser方法执行了
//        selectUser方法执行了
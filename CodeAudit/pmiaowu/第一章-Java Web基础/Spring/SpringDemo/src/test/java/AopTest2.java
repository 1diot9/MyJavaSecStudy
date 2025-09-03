// 第四步
// 创建一个测试类AopTest2进行测试
// 目录: ./SpringDemo/src/test/java/
// 文件名: AopTest2.java
import com.test.AOP.注解例二.AopConfiguration2;
import com.test.AOP.注解例二.Landlord2;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AopTest2 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AopConfiguration2.class);

        // 房东想出租房屋时的结果
        System.out.println("-----房东想出租房屋时的结果-----");
        Landlord2 obj = (Landlord2) context.getBean("landlord2");
        obj.service();

        System.out.println(" ");

        // 房东不想出租房屋时的结果
        System.out.println("-----房东不想出租房屋时的结果-----");
        Landlord2 obj2 = (Landlord2) context.getBean("landlord2");
        obj2.setPrintThrow(true);
        obj.service();
    }
}




// 运行结果
//-----房东想出租房屋时的结果-----
//房屋中介,带租客看房
//        房屋中介,与租客谈价格
//房东,和租客签合同
//        房东,收租客房租
//房屋中介,把钥匙给租客
//        房屋中介,交税给国家
//房屋中介,准备将房间租赁合同上报国家相关单位
//
//-----房东不想出租房屋时的结果-----
//房屋中介,带租客看房
//        房屋中介,与租客谈价格
//房屋中介,意外事件处理-房东不想把房间租给这个人
//        房屋中介,准备将房间租赁合同上报国家相关单位
//Exception in thread "main" java.lang.IllegalArgumentException
//at com.test.AOP.注解例二.Landlord2.printThrowException(Landlord2.java:29)
//at com.test.AOP.注解例二.Landlord2.service(Landlord2.java:22)
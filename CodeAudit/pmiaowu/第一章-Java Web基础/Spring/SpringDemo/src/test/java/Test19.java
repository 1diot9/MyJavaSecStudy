// 第四步
// 创建一个测试类Test19进行测试
// 目录: ./SpringDemo/src/test/java/
// 文件名: Test19.java
import com.test.依赖注入.Set方式注入.Student;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test19 {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext8.xml");
        Student student = (Student) context.getBean("studentTest");
        System.out.println(student.toString());
    }
}




// 运行结果
//Student{name='何九', person=Person{name='马八', age='12'}, arr=[AAA, BBB, CCC], myList=[111, 222, 333], myMap={ddd=dddd, fff=ffff, ggg=gggg}, mySet=[111, 222, 333], wife='null', myPro={qqq=qqqq, iii=iiii, hhh=hhhh}}
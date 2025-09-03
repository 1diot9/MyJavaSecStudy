// 第四步
// 创建一个测试类Test17进行测试
// 目录: ./SpringDemo/src/test/java/
// 文件名: Test17.java
import com.test.依赖注入.构造函数注入.TextEditor;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test17 {
    public static void main(String[] args) {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("applicationContext6.xml");
        TextEditor te = (TextEditor) context.getBean("textEditorTest");
        te.spellCheck();
    }
}




// 运行结果
//SpellChecker内部-无参构造函数
//TextEditor内部-有参构造函数
//        拼写检查
package factory.beanFactory;

import javax.el.ELProcessor;

public class EL_test {
    public static void main(String[] args) {
        ELProcessor elProcessor = new ELProcessor();
        // 将 Runtime 对象注入到 EL 上下文中
        elProcessor.defineBean("runtime", Runtime.getRuntime());

        // 使用 EL 表达式调用 exec 方法
        String expression = "${runtime.exec('calc')}";
        elProcessor.eval(expression);
    }
}

// 第一步
// 创建个SpellChecker类,给其它类使用
// 目录: ./SpringDemo/src/main/java/com/test/依赖注入/构造函数注入/
// 文件名: SpellChecker.java
package com.test.依赖注入.构造函数注入;

public class SpellChecker {
    public SpellChecker() {
        System.out.println("SpellChecker内部-无参构造函数");
    }

    public void checkSpelling() {
        System.out.println("拼写检查");
    }
}
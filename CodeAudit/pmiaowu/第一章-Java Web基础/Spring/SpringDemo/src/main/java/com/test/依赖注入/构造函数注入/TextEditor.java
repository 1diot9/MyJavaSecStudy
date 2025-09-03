// 第二步
// 创建个TextEditor类,在构造方法中引用SpellChecker类
// 目录: ./SpringDemo/src/main/java/com/test/依赖注入/构造函数注入/
// 文件名: TextEditor.java
package com.test.依赖注入.构造函数注入;

public class TextEditor {
    private SpellChecker spellChecker;

    public TextEditor(SpellChecker spellChecker, String name) {
        System.out.println("TextEditor内部-有参构造函数");
        System.out.println("name: " + name);
        this.spellChecker = spellChecker;
    }

    public void spellCheck() {
        this.spellChecker.checkSpelling();
    }
}
package com.idiot9;

import javassist.*;


public class CreateMyTest {
    public String address;

    public CreateMyTest() {
        System.out.println("ctConstrutor");
    }

    public static void main(String[] args) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass MyTest = pool.makeClass("MyTest");

        //添加字段
        CtField name = new CtField(pool.get("java.lang.String"), "name", MyTest);
        name.setModifiers(Modifier.PRIVATE);
        MyTest.addField(name, CtField.Initializer.constant("1diot9"));

        CtField address = CtField.make("public String address;", MyTest);
        MyTest.addField(address);

        //添加构造方法
        CtConstructor ctConstructor = new CtConstructor(new CtClass[]{}, MyTest);
        ctConstructor.setBody("System.out.println(\"ctConstrutor\");");
        MyTest.addConstructor(ctConstructor);



        //添加方法
        CtMethod hello = new CtMethod(CtClass.voidType, "hello", new CtClass[]{}, MyTest);
        hello.setModifiers(Modifier.PUBLIC);
        hello.setBody("System.out.println(\"hello\" + name);");
        MyTest.addMethod(hello);

        CtMethod tmp = CtMethod.make("    public void tmp(){\n" +
                "        System.out.println(\"tmp\");\n" +
                "    }", MyTest);
        MyTest.addMethod(tmp);

        //添加静态代码块
        CtConstructor staticBlock = MyTest.makeClassInitializer();
        staticBlock.setBody("System.out.println(\"staticBlock\");");

        String s = "System.out.println(\"staticBlock2\");";
        MyTest.makeClassInitializer().insertAfter(s);


        //添加父类
        CtClass superClass = pool.get("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet");
        MyTest.setSuperclass(superClass);


        //输出字节码
        MyTest.writeFile("./src/main/java/");


    }

    public void tmp(){
        System.out.println("tmp");
    }
}

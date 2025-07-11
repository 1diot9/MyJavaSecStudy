package com.idiot9;

import javassist.*;
import org.apache.commons.beanutils.BeanComparator;

public class CreateOutPackage {
    public static void main(String[] args) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        //insertClassPath影响类搜索范围，pom里导入的依赖，默认在搜索范围
        pool.insertClassPath("D:\\sec_software\\environment\\repository\\commons-beanutils\\commons-beanutils\\1.9.4\\commons-beanutils-1.9.4.jar");
        //importPackage影响代码写法，是写全限定名还是写SimpleName
        pool.importPackage("org.apache.commons.beanutils");

        CtClass ctClass = pool.makeClass("Test");

        String staticBlock = "BeanComparator beanComparator = new BeanComparator();";
        ctClass.makeClassInitializer().insertAfter(staticBlock);

    }

    static {
        BeanComparator beanComparator = new BeanComparator();
    }
}

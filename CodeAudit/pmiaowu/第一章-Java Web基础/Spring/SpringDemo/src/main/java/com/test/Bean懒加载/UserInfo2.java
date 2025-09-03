// 第一步
// 在./SpringDemo/src/main/java/com/test/Bean懒加载/创建个UserInfo2.java
// 目录: ./SpringDemo/src/main/java/com/test/Bean懒加载/
// 文件名: UserInfo2.java
package com.test.Bean懒加载;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 注册bean名字为: userInfo2Test
 */
@Component("userInfo2Test")
@Lazy(value = false)
public class UserInfo2 {
    public UserInfo2() {
        System.out.println("UserInfo2被初始化了");
    }
}
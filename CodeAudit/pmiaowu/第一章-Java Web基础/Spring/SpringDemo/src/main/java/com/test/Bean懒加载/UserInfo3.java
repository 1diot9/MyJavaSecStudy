// 第一步
// 在./SpringDemo/src/main/java/com/test/Bean懒加载/创建个UserInfo3.java
// 目录: ./SpringDemo/src/main/java/com/test/Bean懒加载/
// 文件名: UserInfo3.java
package com.test.Bean懒加载;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 注册bean名字为: userInfo3Test
 */
@Component("userInfo3Test")
/**
 * @Lazy/@Lazy(value = true)都可以
 */
@Lazy
public class UserInfo3 {
    public UserInfo3() {
        System.out.println("UserInfo3被初始化了");
    }
}
// 第二步
// 目录: ./SpringDemo/src/main/java/com/test/Bean自动扫描/
// 文件名: IUserDaoImpl.java
package com.test.Bean自动扫描;

import org.springframework.stereotype.Repository;

/**
 * 数据库访问层
 */
@Repository
public class IUserDaoImpl implements IUserDao {
    @Override
    public void addUser() {
        System.out.println("addUser方法执行了");
    }

    @Override
    public void delUser() {
        System.out.println("delUser方法执行了");
    }

    @Override
    public void updateUser() {
        System.out.println("updateUser方法执行了");
    }

    @Override
    public void queryUser() {
        System.out.println("selectUser方法执行了");
    }
}
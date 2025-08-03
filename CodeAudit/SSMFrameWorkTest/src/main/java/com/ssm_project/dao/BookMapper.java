package com.ssm_project.dao;

import com.ssm_project.pojo.Books;

import java.util.List;

public interface BookMapper {
    //增加一个Book
    int addBook(Books book);

    //根据id删除一个Book
    int deleteBookById(String id);

    //更新Book
    int updateBook(Books books);

    //根据id查询,返回一个Book
    Books queryBookById(String id);

    //查询全部Book,返回list集合
    List<Books> queryAllBook();
}

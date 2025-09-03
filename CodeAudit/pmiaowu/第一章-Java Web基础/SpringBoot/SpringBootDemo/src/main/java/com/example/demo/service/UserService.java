package com.example.demo.service;

import com.example.demo.bean.UserBean;

public interface UserService {
    UserBean loginIn(String username,String password);
}

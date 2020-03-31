package com.lzp.service;

import com.lzp.po.User;

public interface UserService {
    // 校验用户名密码
    User checkUser(String username, String password);
}

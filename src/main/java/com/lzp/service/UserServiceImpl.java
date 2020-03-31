package com.lzp.service;

import com.lzp.dao.UserRepository;
import com.lzp.po.User;
import com.lzp.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 校验用户名密码
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public User checkUser(String username, String password) {
        User user = this.userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));
        return user;
    }

}

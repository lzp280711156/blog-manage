package com.lzp.dao;

import com.lzp.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // 校验用户名密码
    User findByUsernameAndPassword(String username, String password);
}

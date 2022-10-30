package com.engine.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.engine.domain.User;

import java.util.Map;

public interface UserService extends IService<User> {
    Map<String,String> login(String username, String password);
    boolean logout();
    void saveUser(User user);
}

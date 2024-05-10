package com.example.skydelivery.service;

import com.example.skydelivery.dto.UserLoginDTO;
import com.example.skydelivery.entity.User;

public interface UserService {
    /**
     * 用户登录
     */
    User wxLogin(UserLoginDTO userLoginDTO);
}

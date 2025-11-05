package com.example.mall.service;

import com.example.mall.common.enums.RegisterResultEnum;
import com.example.mall.controller.UserRegisterDTO;
import com.example.mall.entity.User;

public interface Authenticated {
    public RegisterResultEnum register(UserRegisterDTO userDTO);
    public Object login(String username, String password);
}

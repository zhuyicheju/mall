package com.example.mall.service.impl;

import com.example.mall.common.converter.UserConverter;
import com.example.mall.common.enums.RegisterResultEnum;
import com.example.mall.controller.UserRegisterDTO;
import com.example.mall.entity.User;
import com.example.mall.service.Authenticated;

public class UserService implements Authenticated {
    public RegisterResultEnum register(UserRegisterDTO userDTO){
        User user = UserConverter.convert(userDTO);
        
        return RegisterResultEnum.SUCCESS;
    }
}

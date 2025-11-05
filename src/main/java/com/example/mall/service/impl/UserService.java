package com.example.mall.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.mall.common.JwtUtils;
import com.example.mall.common.MallException;
import com.example.mall.common.converter.UserConverter;
import com.example.mall.common.enums.HttpResultCode;
import com.example.mall.common.enums.RegisterResultEnum;
import com.example.mall.controller.LoginResponse;
import com.example.mall.controller.UserLoginDTO;
import com.example.mall.controller.UserRegisterDTO;
import com.example.mall.entity.User;
import com.example.mall.mapper.UserMapper;
import com.example.mall.service.Authenticated;
import com.example.mall.common.MallException;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserService implements Authenticated {

    @Resource
    private UserMapper userMapper;

    @Resource
    PasswordEncoder passwordEncoder;

    @Resource
    JwtUtils jwtUtils;

    public RegisterResultEnum register(UserRegisterDTO userDTO){     
        User user = UserConverter.convert(userDTO, passwordEncoder);
        User existUser = userMapper.getUserByName(user.getUsername());
        if(existUser != null){
            return RegisterResultEnum.NAME_ALREADY_USED;
        }
        userMapper.addUser(user);
        return RegisterResultEnum.SUCCESS;
    }

    public LoginResponse login(UserLoginDTO userLoginDTO){
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();
        User user = userMapper.getUserByName(username);
        if(user == null){
            throw new MallException(HttpResultCode.NOT_FOUND, "未找到用户");
        }
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new MallException(HttpResultCode.BAD_REQUEST, "密码错误");
        }
        LoginResponse loginResponse = new LoginResponse();
        String token = jwtUtils.generateToken(username);
        loginResponse.setToken(token);
        loginResponse.setNickname(user.getNickname());
        loginResponse.setUserId(user.getId());
        loginResponse.setUsername(user.getUsername());
        return loginResponse;
    }
}

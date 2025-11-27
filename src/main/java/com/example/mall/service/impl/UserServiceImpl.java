package com.example.mall.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.mall.common.MallException;
import com.example.mall.common.Utils.JwtUtils;
import com.example.mall.common.converter.UserConverter;
import com.example.mall.common.enums.HttpResultCode;
import com.example.mall.common.enums.RegisterResultEnum;
import com.example.mall.controller.LoginResponse;
import com.example.mall.controller.UserLoginDTO;
import com.example.mall.controller.UserRegisterDTO;
import com.example.mall.entity.User;
import com.example.mall.mapper.UserMapper;
import com.example.mall.service.UserService;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    PasswordEncoder passwordEncoder;

    @Resource
    JwtUtils jwtUtils;

    public RegisterResultEnum register(UserRegisterDTO userDTO){     
        log.info("register - 开始: userDTO={}", userDTO);
        
        User user = UserConverter.convert(userDTO, passwordEncoder);
        log.debug("register - 转换UserRegisterDTO为User实体: user={}", user);
        
        User existUser = userMapper.getUserByName(user.getUsername());
        log.debug("register - 查询用户名是否存在: username={}, existUser={}", user.getUsername(), existUser);
        
        if(existUser != null){
            log.warn("register - 用户名已存在: username={}", user.getUsername());
            log.info("register - 结束: 返回NAME_ALREADY_USED");
            return RegisterResultEnum.NAME_ALREADY_USED;
        }
        
        userMapper.addUser(user);
        log.info("register - 用户注册成功: username={}", user.getUsername());
        log.info("register - 结束: 返回SUCCESS");
        return RegisterResultEnum.SUCCESS;
    }

    public LoginResponse login(UserLoginDTO userLoginDTO){
        log.info("login - 开始: userLoginDTO={}", userLoginDTO);
        
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();
        log.debug("login - 登录参数: username={}", username);
        
        User user = userMapper.getUserByName(username);
        log.debug("login - 查询用户: username={}, user={}", username, user);
        
        if(user == null){
            log.error("login - 用户不存在: username={}", username);
            throw new MallException(HttpResultCode.NOT_FOUND, "未找到用户");
        }
        
        if(!passwordEncoder.matches(password, user.getPassword())){
            log.error("login - 密码错误: username={}", username);
            throw new MallException(HttpResultCode.BAD_REQUEST, "密码错误");
        }
        
        LoginResponse loginResponse = new LoginResponse();
        String token = jwtUtils.generateToken(username);
        loginResponse.setToken(token);
        loginResponse.setNickname(user.getNickname());
        loginResponse.setUserId(user.getId());
        loginResponse.setUsername(user.getUsername());
        log.debug("login - 生成登录响应: loginResponse={}", loginResponse);
        
        log.info("login - 登录成功: username={}", username);
        log.info("login - 结束: 返回LoginResponse");
        return loginResponse;
    }
}
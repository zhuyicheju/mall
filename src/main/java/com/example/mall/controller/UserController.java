package com.example.mall.controller;

import org.springframework.security.config.authentication.UserServiceBeanDefinitionParser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mall.common.ApiResponse;
import com.example.mall.common.enums.HttpResultCode;
import com.example.mall.common.enums.RegisterResultEnum;
import com.example.mall.service.UserService;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@Slf4j
public class UserController {

    @Resource
    UserService userService;

    @PostMapping("/register")
    public ApiResponse<RegisterResultEnum> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO){
        log.info("register - 开始: userRegisterDTO={}", userRegisterDTO);
        log.info("Receive message");
        RegisterResultEnum result = userService.register(userRegisterDTO);
        log.debug("register - 注册结果: {}", result);
        
        if(result != RegisterResultEnum.SUCCESS){
            log.warn("register - 注册失败: result={}", result);
            log.info("register - 结束: 返回错误响应");
            return ApiResponse.error(HttpResultCode.BAD_REQUEST, result);
        }
        
        log.info("register - 注册成功");
        log.info("register - 结束: 返回成功响应");
        return ApiResponse.success(HttpResultCode.OK, result);
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody UserLoginDTO userLoginDTO){
        log.info("login - 开始: userLoginDTO={}", userLoginDTO);
        
        try{
            LoginResponse loginResponse = userService.login(userLoginDTO);
            log.info("login - 登录成功: loginResponse={}", loginResponse);
            log.info("login - 结束: 返回成功响应");
            return ApiResponse.success(HttpResultCode.OK, loginResponse);
        }catch(Exception e){
            log.error("login - 登录异常", e);
            log.info("login - 结束: 返回错误响应");
            return ApiResponse.error(HttpResultCode.BAD_REQUEST, null);
        }
    }
}
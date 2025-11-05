package com.example.mall.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mall.common.ApiResponse;
import com.example.mall.common.enums.HttpResultCode;
import com.example.mall.common.enums.RegisterResultEnum;
import com.example.mall.service.Authenticated;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Resource
    Authenticated userService;

    @PostMapping("/register")
    public ApiResponse<RegisterResultEnum> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO){
        RegisterResultEnum result = userService.register(userRegisterDTO);
        if(result != RegisterResultEnum.SUCCESS){
            return ApiResponse.error(HttpResultCode.BAD_REQUEST, result);
        }
        return ApiResponse.success(HttpResultCode.OK, result);
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody UserLoginDTO userLoginDTO){
        try{
            LoginResponse loginResponse = userService.login(userLoginDTO);
            return ApiResponse.success(HttpResultCode.OK, loginResponse);
        }catch(Exception e){
            return ApiResponse.error(HttpResultCode.BAD_REQUEST, null);
        }
    }
}

package com.example.mall.common.converter;

import java.time.LocalDateTime;

import com.example.mall.controller.UserRegisterDTO;
import com.example.mall.entity.User;

public class UserConverter {
    /**
     * 将UserRegisterDTO转换为User实体
     */
    public static User convert(UserRegisterDTO dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        // 映射DTO中已有的字段（字段名一致，直接赋值）
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setNickname(dto.getNickname());
        user.setPhone(dto.getPhone());

        // 设置Entity独有的字段（根据业务逻辑赋值）
        user.setId(null); // id通常由数据库自增，暂不设置
        user.setStatus(1); // 注册默认状态：1-正常（可根据实际业务调整）
        user.setCreateTime(LocalDateTime.now()); // 创建时间为当前时间
        user.setLastLoginTime(null); // 首次注册无登录时间

        return user;
    }
}

package com.example.mall.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.mall.entity.User;

@Mapper
public interface UserMapper {
    public User getUserByName(String name);
    public int addUser(User user);
    public int updateUserById(User user);
}

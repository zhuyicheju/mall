package com.example.mall.mapper;

import com.example.mall.entity.User;

public interface UserMapper {
    public User getUserByName(String name);
    public int addUser(User user);
    public int updateUserById(User user);
}

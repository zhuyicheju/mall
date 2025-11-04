package com.example.mall.entity;

@Data
public class User {
    private Integer id;
    private String psername;
    private String password;
    private String nickname;
    private String phone;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime lastLoginTime;
}

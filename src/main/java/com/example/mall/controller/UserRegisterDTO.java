package com.example.mall.controller;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterDTO {
    
    @NotBlank
    @Size(min=2, max=20)
    private String username;

    @NotBlank
    @Size(min=6, max=20)
    @Pattern(regexp = "^[a-zA-Z0-9]{6,16}$", message = "密码必须是6-16位数字或字母")
    private String password;

    @NotBlank
    @Size(min=2, max=20)
    private String nickname;

    @NotBlank
    @Size(min=11, max=11)
    private String phone;
}

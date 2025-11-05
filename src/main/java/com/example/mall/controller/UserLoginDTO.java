package com.example.mall.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginDTO {
    @NotBlank 
    private String username;

    @NotBlank
    private String password;
}

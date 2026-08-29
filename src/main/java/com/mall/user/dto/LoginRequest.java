package com.mall.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * ClassName:LoginRequest
 * Package:com.mall.user.dto
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 12:39
 * @Version: v1.0
 *
 */
@Data
public class LoginRequest {
    @NotBlank(message = "Username not be null")
    private String username;

    @NotBlank(message = "password not be null")
    private String password;
}

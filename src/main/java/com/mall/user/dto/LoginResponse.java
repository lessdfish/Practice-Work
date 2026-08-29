package com.mall.user.dto;

import lombok.Data;

/**
 * ClassName:LoginResponse
 * Package:com.mall.user.dto
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 12:39
 * @Version: v1.0
 *
 */
@Data
public class LoginResponse {
    private String token;

    public LoginResponse(String token) {
        this.token = token;
    }
}

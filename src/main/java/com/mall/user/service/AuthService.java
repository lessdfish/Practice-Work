package com.mall.user.service;

import com.mall.user.dto.LoginRequest;
import com.mall.user.dto.LoginResponse;

/**
 * ClassName:AuthService
 * Package:com.mall.user.service
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 13:25
 * @Version: v1.0
 *
 */
public interface AuthService {
    LoginResponse login(LoginRequest request);
}

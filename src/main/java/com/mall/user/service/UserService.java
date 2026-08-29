package com.mall.user.service;

import com.mall.user.domain.User;
import com.mall.user.dto.RegisterRequest;

/**
 * ClassName:UserService
 * Package:com.mall.user.service
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 12:38
 * @Version: v1.0
 *
 */
public interface UserService {
    User register(RegisterRequest request);

    User findByUsername(String username);
}

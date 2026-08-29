package com.mall.user.controller;

import com.mall.common.result.Result;
import com.mall.user.domain.User;
import com.mall.user.dto.LoginRequest;
import com.mall.user.dto.LoginResponse;
import com.mall.user.dto.RegisterRequest;
import com.mall.user.service.AuthService;
import com.mall.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName:AuthController
 * Package:com.mall.user.controller
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 12:38
 * @Version: v1.0
 *
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public Result<User> register(@Valid @RequestBody RegisterRequest request){
        return Result.success(userService.register(request));
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request){
        return Result.success(authService.login(request));
    }
}

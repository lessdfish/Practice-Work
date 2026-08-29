package com.mall.user.service;

import com.mall.security.JwtTokenProvider;
import com.mall.security.MallUserDetails;
import com.mall.user.dto.LoginRequest;
import com.mall.user.dto.LoginResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * ClassName:AuthServiceImpl
 * Package:com.mall.user.service
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 13:26
 * @Version: v1.0
 *
 */
@Service
public class AuthServiceImpl implements AuthService{
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        MallUserDetails userDetails = (MallUserDetails) authentication.getPrincipal();
        String token = jwtTokenProvider.generateToken(userDetails);
        return new LoginResponse(token);
    }
}

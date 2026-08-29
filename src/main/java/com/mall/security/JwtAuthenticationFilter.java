package com.mall.security;

import ch.qos.logback.core.util.StringUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * ClassName:JwtAuthenticationFilter
 * Package:com.mall.security
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 13:36
 * @Version: v1.0
 *
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final MallUserDetailService userDetailService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, MallUserDetailService userDetailService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailService = userDetailService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (!StringUtils.hasText(authorization) || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(
                    request,
                    response
            );
            return;
        }
        String token = authorization.substring(7);
        if (!jwtTokenProvider.validateToken(token)) {
            filterChain.doFilter(request,response);
            return;
        }
        String username = jwtTokenProvider.getUsername(token);

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            MallUserDetails userDetails = (MallUserDetails) userDetailService.loadUserByUsername(username);
            if (userDetails.isEnabled()) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request,response);
    }
}

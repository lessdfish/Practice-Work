package com.mall.common.ratelimit;

import com.mall.common.exception.BusinessException;
import com.mall.common.exception.ErrorCode;
import com.mall.common.redis.RedisKeyConstants;
import com.mall.config.SeckillRateLimitProperties;
import com.mall.security.MallUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * ClassName:SeckillRateLimitInterceptor
 * Package:com.mall.common.ratelimit
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/4 - 18:56
 * @Version: v1.0
 *
 */
@Component
@RequiredArgsConstructor
public class SeckillRateLimitInterceptor implements HandlerInterceptor {
    private final RedisTokenBucketRateLimiter rateLimiter;
    private final SeckillRateLimitProperties properties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod) || !handlerMethod.hasMethodAnnotation(SeckillRateLimit.class)) {
            return true;
        }
        if (!properties.isEnabled()) {
            return true;
        }

        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (!(principal instanceof MallUserDetails user)) {
            return true;
        }

        checkIp(request.getRemoteAddr());

        checkUser(user.getUserId());
        return true;
    }

    private void checkIp(String ip){
        var bucket = properties.getIp();

        boolean allowed = rateLimiter.tryAcquire(
                RedisKeyConstants.seckillIpRateLimit(ip),
                bucket.getCapacity(),
                bucket.getRefillRate()
        );

        if (!allowed) {
            throw new BusinessException(ErrorCode.SECKILL_RATE_LIMITED);
        }
    }

    private void checkUser(Long userId){
        var bucket = properties.getUser();

        boolean allowed = rateLimiter.tryAcquire(
                RedisKeyConstants.seckillUserRateLimit(userId),
                bucket.getCapacity(),
                bucket.getRefillRate()
        );

        if (!allowed) {
            throw new BusinessException(ErrorCode.SECKILL_RATE_LIMITED);
        }
    }
}

package com.mall.common.ratelimit;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ClassName:RedisTokenBucketRateLimiter
 * Package:com.mall.common.ratelimit
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/4 - 18:48
 * @Version: v1.0
 *
 */
@Component
@RequiredArgsConstructor
public class RedisTokenBucketRateLimiter {

    private static final long BUCKET_TTL_MS = 2 * 60 * 1000L;

    private static final DefaultRedisScript<Long> TOKEN_BUCKET_SCRIPT;

    static {
        TOKEN_BUCKET_SCRIPT = new DefaultRedisScript<>();
        TOKEN_BUCKET_SCRIPT.setLocation(
                new ClassPathResource("scripts/token_bucket.lua")
        );
        TOKEN_BUCKET_SCRIPT.setResultType(Long.class);
    }

    private final StringRedisTemplate redisTemplate;

    public boolean tryAcquire(String key,int capacity,double refillRate){
        Long result = redisTemplate.execute(
                TOKEN_BUCKET_SCRIPT,
                List.of(key),
                String.valueOf(capacity),
                String.valueOf(refillRate),
                "1",
                String.valueOf(BUCKET_TTL_MS)
        );

        return result != null && result == 1L;
    }
}

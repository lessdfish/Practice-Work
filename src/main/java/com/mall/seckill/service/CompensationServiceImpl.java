package com.mall.seckill.service;

import com.mall.common.redis.RedisKeyConstants;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ClassName:CompensationServiceImpl
 * Package:com.mall.seckill.service
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/1 - 23:32
 * @Version: v1.0
 *
 */
@Service
public class CompensationServiceImpl implements CompensationService{
    public static final DefaultRedisScript<Long> COMPENSATE_SCRIPT;

    static {
        COMPENSATE_SCRIPT = new DefaultRedisScript<>();
        COMPENSATE_SCRIPT.setLocation(new ClassPathResource("scripts/seckill_compensate.lua"));
        COMPENSATE_SCRIPT.setResultType(Long.class);
    }

    private final StringRedisTemplate redisTemplate;

    public CompensationServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean compensate(Long activityId, Long userId) {
        Long result = redisTemplate.execute(COMPENSATE_SCRIPT,
                List.of(RedisKeyConstants.seckillStock(activityId),
                        RedisKeyConstants.seckillUsers(activityId)),
                String.valueOf(userId));
        return result!=null && result == 1L;
    }
}

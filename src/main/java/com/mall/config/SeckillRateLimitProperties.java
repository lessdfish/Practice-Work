package com.mall.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.convert.Bucket;
import org.springframework.stereotype.Component;

/**
 * ClassName:SeckillRateLimitProperties
 * Package:com.mall.config
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/4 - 18:34
 * @Version: v1.0
 *
 */
@Data
@Component
@ConfigurationProperties(prefix = "mall.seckill.rate-limit")
public class SeckillRateLimitProperties {

    private boolean enabled = true;

    private Bucket user = new Bucket();
    private Bucket ip = new Bucket();

    @Data
    public static class Bucket{
        private int capacity;
        private double refillRate;
    }
}

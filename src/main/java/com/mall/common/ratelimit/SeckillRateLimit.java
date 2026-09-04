package com.mall.common.ratelimit;

import java.lang.annotation.*;

/**
 * ClassName:SeckillRateLimit
 * Package:com.mall.common.ratelimit
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/4 - 18:55
 * @Version: v1.0
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SeckillRateLimit {
}

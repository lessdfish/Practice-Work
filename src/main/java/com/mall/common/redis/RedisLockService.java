package com.mall.common.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;

/**
 * ClassName:RedisLockService
 * Package:com.mall.common.redis
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 16:11
 * @Version: v1.0
 *
 */
@Component
public class RedisLockService {
    private final StringRedisTemplate stringRedisTemplate;
    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT;

    static {
        UNLOCK_SCRIPT = new DefaultRedisScript<>();
        UNLOCK_SCRIPT.setScriptText("""
                if redis.call('get',KEYS[1]) == ARGV[1] then
                    return redis.call('del',KEYS[1])
                else
                    return 0
                end
                """);
        UNLOCK_SCRIPT.setResultType(Long.class);
    }

    public RedisLockService(StringRedisTemplate stringRedisTemplate){
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public String tryLock(String key, Duration ttl){
        String value = UUID.randomUUID().toString();
        Boolean success = stringRedisTemplate.opsForValue().setIfAbsent(key,value,ttl);
        if (Boolean.TRUE.equals(success)) {
            return value;
        }
        return null;
    }

    public void unlock(String key,String value){
        stringRedisTemplate.execute(UNLOCK_SCRIPT, Collections.singletonList(key),value);
    }
}

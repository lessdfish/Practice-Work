package com.mall.seckill.service;

import com.mall.common.exception.BusinessException;
import com.mall.common.exception.ErrorCode;
import com.mall.common.redis.RedisKeyConstants;
import com.mall.seckill.domain.SeckillRequestStatus;
import com.mall.seckill.dto.SeckillResultResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

/**
 * ClassName:SeckillResultServiceImpl
 * Package:com.mall.seckill.service
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/31 - 22:54
 * @Version: v1.0
 *
 */
@Service
public class SeckillResultServiceImpl implements SeckillResultService{
    public static final Duration RESULT_TTL = Duration.ofDays(1);
    private final StringRedisTemplate redisTemplate;

    public SeckillResultServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void createQueue(String requestId, Long activityId, Long userId) {
        String key = RedisKeyConstants.seckillResult(requestId);
        redisTemplate.opsForHash().putAll(key, Map.of("userId",String.valueOf(userId),
                "activityId",String.valueOf(activityId),
                "status", SeckillRequestStatus.QUEUED.name()));

        redisTemplate.expire(key,RESULT_TTL);
    }

    @Override
    public void markProcessing(String requestId) {
        redisTemplate.opsForHash().put(RedisKeyConstants.seckillResult(requestId),"status",SeckillRequestStatus.PROCESSING.name());
    }

    @Override
    public void markSuccess(String requestId, Long orderId) {
        String key = RedisKeyConstants.seckillResult(requestId);
        redisTemplate.opsForHash().put(key,"status",SeckillRequestStatus.SUCCESS.name());

        if (orderId!=null) {
            redisTemplate.opsForHash().put(key,"orderId",String.valueOf(orderId));
        }

        redisTemplate.opsForHash().put(key,"message","Seckill Success");
    }

    @Override
    public void markFailed(String requestId, String message) {
        String key = RedisKeyConstants.seckillResult(requestId);
        redisTemplate.opsForHash().put(key,"status",SeckillRequestStatus.FAILED.name());
        redisTemplate.opsForHash().put(key,"message",message);
    }

    @Override
    public void markPublishUnknown(String requestId) {
        redisTemplate.opsForHash().put(RedisKeyConstants.seckillResult(requestId),"status",SeckillRequestStatus.PUBLISH_UNKNOWN.name());
    }

    @Override
    public SeckillResultResponse getResult(String requestId, Long userId) {
        String key = RedisKeyConstants.seckillResult(requestId);
        Map<Object,Object> result = redisTemplate.opsForHash().entries(key);
        if (result.isEmpty()) {
            throw new BusinessException(ErrorCode.SECKILL_RESULT_NOT_FOUND);
        }
        Long ownerId = Long.valueOf(result.get("userId").toString());

        if (!ownerId.equals(userId)) {
            throw new BusinessException(SECKILL_RESULT_NOT_FOUND);
        }

        SeckillResultResponse response = new SeckillResultResponse();

        response.setRequestId(requestId);
        response.setStatus(result.get("status").toString());
        Object orderId = result.get("orderId");
        if (orderId!=null) {
            response.setOrderId(Long.valueOf(orderId.toString()));
        }

        Object message = result.get("message");

        if(message!=null){
            response.setMessage(message.toString());
        }
        return response;
    }
}

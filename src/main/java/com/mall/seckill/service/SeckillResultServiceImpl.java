package com.mall.seckill.service;

import com.mall.common.exception.BusinessException;
import com.mall.common.exception.ErrorCode;
import com.mall.common.redis.RedisKeyConstants;
import com.mall.seckill.domain.SeckillRequestRecord;
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

    private String key(Long activityId,String requestId){
        return RedisKeyConstants.seckillResult(activityId,requestId);
    }

    @Override
    public void markProcessing(Long activityId, String requestId) {
        redisTemplate.opsForHash().put(
                key(activityId,requestId),
                "status",
                SeckillRequestStatus.PROCESSING.name()
        );
    }

    @Override
    public void markSuccess(Long activityId, String requestId, Long orderId) {
        String key = key(activityId,requestId);

        redisTemplate.opsForHash().putAll(
                key,
                Map.of(
                        "status",
                        SeckillRequestStatus.SUCCESS.name(),
                        "orderId",
                        String.valueOf(orderId),
                        "message",
                        "秒杀成功"
                )
        );
    }

    @Override
    public void markFailed(Long activityId, String requestId, String message) {
        redisTemplate.opsForHash().putAll(
                key(activityId,requestId),
                Map.of(
                        "status",
                        SeckillRequestStatus.FAILED.name(),
                        "message",
                        message
                )
        );
    }

    @Override
    public void markPublishUnknown(Long activityId, String requestId) {

    }

    @Override
    public void markFailedCompensated(Long activityId, String requestId) {
        redisTemplate.opsForHash().put(
                key(activityId,requestId),
                "status",
                SeckillRequestStatus.FAILED_COMPENSATED.name()
        );
    }

    @Override
    public SeckillResultResponse getResult(Long activityId, String requestId, Long userId) {
        var result = redisTemplate.opsForHash().entries(key(activityId,requestId));

        if (result.isEmpty()) {
            throw new BusinessException(ErrorCode.SECKILL_RESULT_NOT_FOUND);
        }

        Long ownerId = Long.valueOf(result.get("userId").toString());

        if (!ownerId.equals(userId)) {
            throw new BusinessException(ErrorCode.SECKILL_RESULT_NOT_FOUND);
        }

        var response = new SeckillResultResponse();

        response.setRequestId(requestId);
        response.setStatus(result.get("status").toString());

        if (result.get("orderId") !=null) {
            response.setOrderId(Long.valueOf(result.get("orderId").toString()));
        }
        if (result.get("message") !=null) {
            response.setMessage(result.get("message").toString());
        }
        return response;
    }

    @Override
    public SeckillRequestRecord getRequestRecord(Long activityId, String requestId) {
        return null;
    }
}

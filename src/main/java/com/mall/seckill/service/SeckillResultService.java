package com.mall.seckill.service;

import com.mall.seckill.dto.SeckillResultResponse;

/**
 * ClassName:SeckillResultService
 * Package:com.mall.seckill.service
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/31 - 22:51
 * @Version: v1.0
 *
 */
public interface SeckillResultService {
    void createQueue(String requestId,Long activityId,Long userId);

    void markProcessing(String requestId);

    void markSuccess(String requestId,Long orderId);

    void markFailed(String requestId,String message);

    void markPublishUnknown(String requestId);

    SeckillResultResponse getResult(String requestId,Long userId);
}

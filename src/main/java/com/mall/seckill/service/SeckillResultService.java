package com.mall.seckill.service;

import com.mall.seckill.domain.SeckillRequestRecord;
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
    void markProcessing(Long activityId,String requestId);

    void markSuccess(Long activityId,String requestId,Long orderId);

    void markFailed(Long activityId,String requestId,String message);

    void markPublishUnknown(Long activityId,String requestId);

    void markFailedCompensated(Long activityId,String requestId);

    SeckillResultResponse getResult(Long activityId,String requestId,Long userId);

    SeckillRequestRecord getRequestRecord(Long activityId,String requestId);


}

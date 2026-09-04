package com.mall.seckill.service;

import com.rabbitmq.client.LongString;

/**
 * ClassName:ReconcileService
 * Package:com.mall.seckill.service
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/1 - 23:47
 * @Version: v1.0
 *
 */
public interface ReconcileService {
    void reconcile(Long activityId,String requestId);
}

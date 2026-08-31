package com.mall.seckill.service;

import com.mall.order.domain.Order;

/**
 * ClassName:SeckillService
 * Package:com.mall.seckill.service
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/31 - 00:15
 * @Version: v1.0
 *
 */
public interface SeckillService {
    void preheat(Long activityId);

    Order seckill(Long activityId, Long userId);
}

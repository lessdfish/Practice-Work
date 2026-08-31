package com.mall.seckill.service;

import com.mall.order.domain.Order;
import com.mall.seckill.domain.SeckillActivity;

/**
 * ClassName:SeckillOrderService
 * Package:com.mall.seckill.service
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/31 - 00:00
 * @Version: v1.0
 *
 */
public interface SeckillOrderService {
    Long createOrder(Long userId, Long activityId);

    Long findExistingOrderId(Long userId,Long activityId);
}

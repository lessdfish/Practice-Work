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
    Order createOrder(Long userId, SeckillActivity seckillActivity);
}

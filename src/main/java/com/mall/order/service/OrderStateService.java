package com.mall.order.service;

import com.mall.order.domain.OrderStatus;

/**
 * ClassName:OrderStateService
 * Package:com.mall.order.service
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/4 - 14:51
 * @Version: v1.0
 *
 */
public interface OrderStateService {
    void transition(Long orderId, OrderStatus from,OrderStatus to);
}

package com.mall.order.service;

import com.mall.order.domain.Order;
import com.mall.order.dto.CreateOrderRequest;

/**
 * ClassName:OrderService
 * Package:com.mall.order.service
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/27 - 00:10
 * @Version: v1.0
 *
 */
public interface OrderService {
    Order createOrder(CreateOrderRequest request);
}

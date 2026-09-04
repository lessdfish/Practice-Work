package com.mall.order.service;

/**
 * ClassName:OrderTimeoutService
 * Package:com.mall.order.service
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/4 - 17:07
 * @Version: v1.0
 *
 */
public interface OrderTimeoutService {
    boolean closeTimeoutOrder(Long orderId);
}

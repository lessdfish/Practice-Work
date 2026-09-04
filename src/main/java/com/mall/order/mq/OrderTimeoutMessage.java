package com.mall.order.mq;

/**
 * ClassName:OrderTimeoutMessage
 * Package:com.mall.order.mq
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/4 - 17:25
 * @Version: v1.0
 *
 */
public record OrderTimeoutMessage(Long orderId) {
}

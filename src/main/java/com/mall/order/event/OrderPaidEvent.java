package com.mall.order.event;

import java.time.LocalDateTime;

/**
 * ClassName:OrderPaidEvent
 * Package:com.mall.order.event
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/4 - 17:57
 * @Version: v1.0
 *
 */
public record OrderPaidEvent(
        String eventId,
        Long orderId,
        Long productId,
        Integer quantity,
        LocalDateTime paidTime
) {
}

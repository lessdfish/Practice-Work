package com.mall.order.domain;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * ClassName:OrderStatus
 * Package:com.mall.order.domain
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/4 - 14:34
 * @Version: v1.0
 *
 */
@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    WAIT_PAY(0,"待支付"),
    PAID(1,"已支付"),
    SHIPPED(2,"已发货"),
    COMPLETED(3,"已完成"),
    CANCELLED(4,"已取消"),
    TIMEOUT_CANCELLED(5,"超时关闭");

    private final int code;
    private final String description;

    public boolean canTransitionTo(OrderStatus target){
        return switch (this){
            case WAIT_PAY ->
                    target == PAID
                    || target == CANCELLED
                    || target == TIMEOUT_CANCELLED;
            case PAID -> target == SHIPPED;

            case SHIPPED -> target == COMPLETED;

            default -> false;
        };
    }
}

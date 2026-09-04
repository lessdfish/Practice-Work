package com.mall.payment.dto;

import java.math.BigDecimal;

/**
 * ClassName:CreatePaymentResponse
 * Package:com.mall.payment.dto
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/4 - 15:11
 * @Version: v1.0
 *
 */
public record CreatePaymentResponse(
        String paymentNo,
        Long orderId,
        BigDecimal amount,
        String status
) {
}

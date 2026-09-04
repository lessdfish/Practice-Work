package com.mall.payment.dto;

/**
 * ClassName:PaymentCallbackResponse
 * Package:com.mall.payment.dto
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/4 - 15:15
 * @Version: v1.0
 *
 */
public record PaymentCallbackResponse(
        String paymentNo,
        Long orderId,
        String status
){
}

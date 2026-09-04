package com.mall.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;

import java.math.BigDecimal;

/**
 * ClassName:PaymentCallbackRequest
 * Package:com.mall.payment.dto
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/4 - 15:13
 * @Version: v1.0
 *
 */
public record PaymentCallbackRequest (
        @NotBlank
        String paymentNo,
        @NotBlank
        String thirdPartyTradeNo,
        @NotNull
        BigDecimal amount
){
}

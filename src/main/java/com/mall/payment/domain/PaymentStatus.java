package com.mall.payment.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * ClassName:PaymentStatus
 * Package:com.mall.payment.domain
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/4 - 15:06
 * @Version: v1.0
 *
 */
@Getter
@RequiredArgsConstructor
public enum PaymentStatus {
    WAIT_PAY(0,"待支付"),
    SUCCESS(1,"支付成功"),
    FAILED(2,"支付失败");

    private final int code;
    private final String description;
}

package com.mall.payment.service.impl;

import com.mall.payment.dto.CreatePaymentResponse;
import com.mall.payment.dto.PaymentCallbackRequest;
import com.mall.payment.dto.PaymentCallbackResponse;

/**
 * ClassName:PaymentService
 * Package:com.mall.payment.service.impl
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/4 - 15:26
 * @Version: v1.0
 *
 */
public interface PaymentService {
    CreatePaymentResponse createPayment(Long userId,Long orderId);

    PaymentCallbackResponse handleSuccessCallback(PaymentCallbackRequest request);
}

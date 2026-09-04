package com.mall.payment.controller;

import com.mall.common.result.Result;
import com.mall.payment.dto.CreatePaymentResponse;
import com.mall.payment.service.impl.PaymentService;
import com.mall.security.MallUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName:PaymentController
 * Package:com.mall.payment.controller
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/4 - 15:55
 * @Version: v1.0
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/orders/{orderId}")
    public Result<CreatePaymentResponse> createPayment(
            @PathVariable Long orderId,
            @AuthenticationPrincipal MallUserDetails currentUser
            ){
        return Result.success(
                paymentService.createPayment(
                        currentUser.getUserId(),
                        orderId
                )
        );
    }
}

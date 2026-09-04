package com.mall.payment.controller;

import com.mall.common.result.Result;
import com.mall.payment.dto.PaymentCallbackRequest;
import com.mall.payment.dto.PaymentCallbackResponse;
import com.mall.payment.service.impl.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName:AdminPaymentController
 * Package:com.mall.payment.controller
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/4 - 16:00
 * @Version: v1.0
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/payments")
public class AdminPaymentController {
    private final PaymentService paymentService;

    @PostMapping("/mock-success-callback")
    public Result<PaymentCallbackResponse> mockSuccessCallback(
            @Valid @RequestBody PaymentCallbackRequest request
            ){
        return Result.success(paymentService.handleSuccessCallback(request)
        );
    }
}

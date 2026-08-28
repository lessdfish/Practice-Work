package com.mall.order.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

/**
 * ClassName:CreateOrderRequest
 * Package:com.mall.order.dto
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/27 - 00:04
 * @Version: v1.0
 *
 */
@Data
public class CreateOrderRequest {
    @NotNull(message = "Message ID not be null")
    private Long productId;
    @NotNull(message = "Order Count Not be null")
    @Min(value = 1,message = "Count must be more than zero")
    private Integer quantity;
}

package com.mall.order.dto;

import lombok.Data;

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
    private Long productId;
    private Integer quantity;
}

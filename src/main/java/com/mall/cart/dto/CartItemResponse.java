package com.mall.cart.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * ClassName:CartItemResponse
 * Package:com.mall.cart.dto
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 18:22
 * @Version: v1.0
 *
 */
@Data
public class CartItemResponse {
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;
}

package com.mall.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * ClassName:UpdateCartItemRequest
 * Package:com.mall.cart.dto
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 18:22
 * @Version: v1.0
 *
 */
@Data
public class UpdateCartItemRequest {
    @NotNull(message = "Purchase count not be null")
    @Min(value = 1,message = "Count must more than 0")
    private Integer quantity;
}

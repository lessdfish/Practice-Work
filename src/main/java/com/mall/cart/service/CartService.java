package com.mall.cart.service;

import com.mall.cart.dto.AddCartItemRequest;
import com.mall.cart.dto.CartItemResponse;

import java.util.List;

/**
 * ClassName:CartService
 * Package:com.mall.cart.service
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 18:22
 * @Version: v1.0
 *
 */
public interface CartService {
    void addItem(Long userId, AddCartItemRequest request);

    List<CartItemResponse> listItems(Long userId);

    void updateQuantity(Long userId,Long productId,Integer quantity);

    void removeItem(Long userId,Long productId);

    void clear(Long userId);
}

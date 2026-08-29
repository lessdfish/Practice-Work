package com.mall.cart.controller;

import com.mall.cart.dto.AddCartItemRequest;
import com.mall.cart.dto.CartItemResponse;
import com.mall.cart.dto.UpdateCartItemRequest;
import com.mall.cart.service.CartService;
import com.mall.common.result.Result;
import com.mall.security.MallUserDetails;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName:CartController
 * Package:com.mall.cart.controller
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 18:21
 * @Version: v1.0
 *
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/items")
    public Result<Void> addItem(@AuthenticationPrincipal MallUserDetails currentUser,
                                @Valid @RequestBody AddCartItemRequest request){
        cartService.addItem(currentUser.getUserId(),request);
        return Result.success();
    }

    @GetMapping
    public Result<List<CartItemResponse>> listItems(@AuthenticationPrincipal MallUserDetails currentUser){
        return Result.success(cartService.listItems(currentUser.getUserId()));
    }

    @PutMapping("/items/{productId}")
    public Result<Void> updateQuantity(@AuthenticationPrincipal MallUserDetails currentUser,
                                       @PathVariable Long productId,
                                       @Valid @RequestBody UpdateCartItemRequest request){
        cartService.updateQuantity(currentUser.getUserId(),productId,request.getQuantity());

        return Result.success();
    }

    @DeleteMapping("/items/{productId}")
    public Result<Void> removeItem(@AuthenticationPrincipal MallUserDetails currentUser,
                                   @PathVariable Long productId){
        cartService.removeItem(currentUser.getUserId(),productId);

        return Result.success();
    }

    @DeleteMapping
    public Result<Void> clear(@AuthenticationPrincipal MallUserDetails currentUser){
        cartService.clear(currentUser.getUserId());
        return Result.success();
    }
}

package com.mall.order.controller;

import com.mall.common.result.Result;
import com.mall.order.domain.Order;
import com.mall.order.dto.CreateOrderRequest;
import com.mall.order.service.OrderService;
import com.mall.security.MallUserDetails;
import jakarta.validation.Valid;
import lombok.val;
import org.aspectj.weaver.ast.Or;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName:OrderController
 * Package:com.mall.order.controller
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/27 - 00:22
 * @Version: v1.0
 *
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping
    public Result<Order> createOrder(@AuthenticationPrincipal MallUserDetails currentUser,
                                     @Valid @RequestBody CreateOrderRequest request){
        Order order = orderService.createOrder(currentUser.getUserId(),request);
        return Result.success(order);
    }
    @GetMapping("/my")
    public Result<List<Order>> listMyOrders(@AuthenticationPrincipal MallUserDetails currentUser){
        return Result.success(
                orderService.listMyOrders(currentUser.getUserId())
        );
    }

    @GetMapping("/{id}")
    public Result<Order> getOrder(@AuthenticationPrincipal MallUserDetails currentUser,
                                  @PathVariable Long id){
        return Result.success(orderService.getMyOrder(
                currentUser.getUserId(),
                id
        ));
    }
}

package com.mall.order.controller;

import com.mall.order.domain.Order;
import com.mall.order.dto.CreateOrderRequest;
import com.mall.order.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Order createOrder(@RequestBody CreateOrderRequest request){
        return orderService.createOrder(request);
    }
}

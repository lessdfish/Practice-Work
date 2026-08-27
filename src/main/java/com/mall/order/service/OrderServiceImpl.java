package com.mall.order.service;

import com.mall.common.aspect.OperationLog;
import com.mall.order.domain.Order;
import com.mall.order.dto.CreateOrderRequest;
import com.mall.order.mapper.OrderMapper;
import com.mall.product.domain.Product;
import com.mall.product.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * ClassName:OrderServiceImpl
 * Package:com.mall.order.service
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/27 - 00:11
 * @Version: v1.0
 *
 */
@Service
public class OrderServiceImpl implements OrderService{
    private final OrderMapper orderMapper;
    private final ProductMapper productMapper;

    public OrderServiceImpl(OrderMapper orderMapper, ProductMapper productMapper) {
        this.orderMapper = orderMapper;
        this.productMapper = productMapper;
    }

    @Override
    @OperationLog("Creating Order")
    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(CreateOrderRequest request){
        if (request.getProductId() == null) {
            throw new IllegalArgumentException("productId can't be null");
        }
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new IllegalArgumentException("quantity must more than zero");
        }

        Product product = productMapper.selectById(request.getProductId());
        if (product == null) {
            throw new IllegalArgumentException("The product can't be find");
        }

        if (product.getStatus() !=1) {
            throw new IllegalStateException("Product be cleaned");
        }

        int affectedRows = productMapper.deductStock(request.getProductId(),request.getQuantity());

        if (affectedRows == 0) {
            throw new IllegalArgumentException("Stock under zero");
        }

        BigDecimal amount = product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));

        Order order = new Order();
        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());
        order.setAmount(amount);
        order.setStatus(0);
        int inserted = orderMapper.insert(order);

        if (inserted!=1) {
            throw new IllegalStateException("Order Create Filed");
        }
        return order;
    }
}

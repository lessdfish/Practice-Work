package com.mall.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall.common.aspect.OperationLog;
import com.mall.common.exception.BusinessException;
import com.mall.common.exception.ErrorCode;
import com.mall.config.properties.MallProperties;
import com.mall.order.domain.Order;
import com.mall.order.dto.CreateOrderRequest;
import com.mall.order.mapper.OrderMapper;
import com.mall.product.domain.Product;
import com.mall.product.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

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
    private final MallProperties mallProperties;

    public OrderServiceImpl(OrderMapper orderMapper, ProductMapper productMapper,MallProperties mallProperties) {
        this.orderMapper = orderMapper;
        this.productMapper = productMapper;
        this.mallProperties = mallProperties;
    }

    @Override
    @OperationLog("Creating Order")
    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(Long userId,CreateOrderRequest request){
        Product product = productMapper.selectById(request.getProductId());
        if (product == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        if (product.getStatus() !=1) {
            throw new BusinessException(ErrorCode.PRODUCT_OFF_SHELF);
        }
        Integer maxQuantity = mallProperties.getOrder().getMaxQuantity();

        if (request.getQuantity()> maxQuantity) {
            throw new BusinessException("Single Max Count is "+maxQuantity,ErrorCode.PARAM_ERROR);
        }

        int affectedRows = productMapper.deductStock(request.getProductId(),request.getQuantity());

        if (affectedRows == 0) {
            throw new BusinessException(ErrorCode.STOCK_NOT_ENOUGH);
        }
        BigDecimal amount = product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));

        Order order = new Order();
        order.setUserId(userId);
        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());
        order.setAmount(amount);
        order.setStatus(0);
        int inserted = orderMapper.insert(order);

        if (inserted!=1) {
            throw new BusinessException(ErrorCode.ORDER_CREATE_FAILED);
        }
        return order;
    }

    @Override
    public List<Order> listMyOrders(Long userId) {
        return orderMapper.selectList(
                new LambdaQueryWrapper<Order>()
                        .eq(
                                Order::getUserId,
                                userId
                        )
                        .orderByDesc(Order::getCreateTime)
        );
    }

    @Override
    public Order getMyOrder(Long userId, Long orderId) {
        Order order = orderMapper.selectOne(
                new LambdaQueryWrapper<Order>()
                        .eq(Order::getId,
                                orderId)
                        .eq(Order::getUserId,
                                userId)
                        .last("LIMIT 1")
        );
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        return order;
    }
}

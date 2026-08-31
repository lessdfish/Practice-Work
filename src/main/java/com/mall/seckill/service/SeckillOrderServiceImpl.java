package com.mall.seckill.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.common.exception.BusinessException;
import com.mall.common.exception.ErrorCode;
import com.mall.inventory.service.InventoryService;
import com.mall.order.domain.Order;
import com.mall.order.mapper.OrderMapper;
import com.mall.product.domain.Product;
import com.mall.product.mapper.ProductMapper;
import com.mall.seckill.domain.SeckillActivity;
import com.mall.seckill.mapper.SeckillActivityMapper;
import org.aspectj.weaver.ast.Or;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ClassName:SeckillOrderServiceImpl
 * Package:com.mall.seckill.service
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/31 - 00:01
 * @Version: v1.0
 *
 */
@Service
public class SeckillOrderServiceImpl implements SeckillOrderService{

    private final SeckillActivityMapper activityMapper;
    private final InventoryService inventoryService;
    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;

    public SeckillOrderServiceImpl(SeckillActivityMapper activityMapper, InventoryService inventoryService, ProductMapper productMapper, OrderMapper orderMapper) {
        this.activityMapper = activityMapper;
        this.inventoryService = inventoryService;
        this.productMapper = productMapper;
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createOrder(Long userId, Long activityId) {
        SeckillActivity activity = activityMapper.selectById(activityId);
        if (activity == null) {
            throw new BusinessException(ErrorCode.SECKILL_ACTIVITY_NOT_FOUND);
        }

        int activityUpdated = activityMapper.deductActivityStock(activityId);

        if (activityUpdated == 0) {
            throw new BusinessException(ErrorCode.SECKILL_SOLD_OUT);
        }

        inventoryService.deductStock(activity.getProductId(),1);

        Product product = productMapper.selectById(activity.getProductId());

        if (product == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        if (product.getStatus() != 1) {
            throw new BusinessException(ErrorCode.PRODUCT_OFF_SHELF);
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setSeckillActivityId(activityId);
        order.setProductId(activity.getProductId());
        // Claim: EveryOne 1 quantity
        order.setQuantity(1);
        order.setAmount(activity.getSeckillPrice());
        order.setStatus(0);

        try {
            int inserted = orderMapper.insert(order);
            if(inserted != 1){
                throw new BusinessException(ErrorCode.ORDER_CREATE_FAILED);
            }
            return order.getId();
        }catch (DuplicateKeyException e){
            throw new BusinessException(ErrorCode.SECKILL_DUPLICATE_ORDER);
        }
    }

    @Override
    public Long findExistingOrderId(Long userId, Long activityId) {
        Order order = orderMapper.selectOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getUserId,userId)
                .eq(Order::getSeckillActivityId,activityId)
                .last("LIMIT 1"));
        return order == null?null:order.getId();
    }
}

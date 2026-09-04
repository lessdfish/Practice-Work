package com.mall.order.service;

import com.mall.inventory.service.InventoryService;
import com.mall.order.domain.OrderStatus;
import com.mall.order.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ClassName:OrderTimeoutServiceImpl
 * Package:com.mall.order.service
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/4 - 17:08
 * @Version: v1.0
 *
 */
@Service
@RequiredArgsConstructor
public class OrderTimeoutServiceImpl implements OrderTimeoutService{

    private final OrderMapper orderMapper;
    private final InventoryService inventoryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean closeTimeoutOrder(Long orderId) {
        int updated = orderMapper.timeoutClose(
                orderId,
                OrderStatus.WAIT_PAY.getCode(),
                OrderStatus.TIMEOUT_CANCELLED.getCode());

        if (updated == 0){
            return false;
        }

        inventoryService.releaseStock(orderId);
        return true;
    }
}

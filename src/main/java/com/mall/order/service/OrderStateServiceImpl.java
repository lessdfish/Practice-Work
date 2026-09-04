package com.mall.order.service;

import com.mall.common.exception.BusinessException;
import com.mall.common.exception.ErrorCode;
import com.mall.order.domain.OrderStatus;
import com.mall.order.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * ClassName:OrderStateServiceImpl
 * Package:com.mall.order.service
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/4 - 14:52
 * @Version: v1.0
 *
 */
@Service
@RequiredArgsConstructor
public class OrderStateServiceImpl implements OrderStateService{
    private final OrderMapper orderMapper;

    @Override
    public void transition(Long orderId, OrderStatus from, OrderStatus to) {
        if (!from.canTransitionTo(to)) {
            throw new BusinessException(ErrorCode.ORDER_STATUS_ILLEGAL);
        }

        int updated = orderMapper.transitionStatus(orderId,from.getCode(), to.getCode());

        if (updated!=1) {
            throw new BusinessException(ErrorCode.ORDER_STATUS_CONFLICT);
        }
    }
}

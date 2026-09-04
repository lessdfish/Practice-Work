package com.mall.order.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall.order.domain.Order;
import com.mall.order.domain.OrderStatus;
import com.mall.order.mapper.OrderMapper;
import com.mall.order.service.OrderTimeoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * ClassName:OrderTimeoutScanner
 * Package:com.mall.order.task
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/4 - 17:33
 * @Version: v1.0
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderTimeoutScanner {
    private final OrderMapper orderMapper;
    private final OrderTimeoutService orderTimeoutService;

    @Scheduled(fixedDelay = 60_000)
    public void scan(){
        var orders = orderMapper.selectList(
                new LambdaQueryWrapper<Order>()
                        .eq(
                                Order::getStatus,
                                OrderStatus.WAIT_PAY.getCode()
                        )
                        .le(
                                Order::getExpireTime,
                                LocalDateTime.now()
                        )
                        .last("LIMIT 100")
        );

        for (Order order : orders) {
            try {
                orderTimeoutService.closeTimeoutOrder(order.getId());
            }catch (Exception e){
                log.error("兜底关闭超市订单失败，orderId={}",
                        order.getId(),e);
            }
        }
    }
}

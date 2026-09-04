package com.mall.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.order.domain.Order;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * ClassName:OrderMapper
 * Package:com.mall.order.mapper
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/27 - 00:05
 * @Version: v1.0
 *
 */
public interface OrderMapper extends BaseMapper<Order> {
    @Update("""
            UPDATE mall_order
            SET status = #{toStatus},
                update_time = NOW()
            WHERE id = #{orderId}
              AND status = #{fromStatus}
            """)
    int transitionStatus(
            @Param("orderId") Long orderId,
            @Param("fromStatus") Integer fromStatus,
            @Param("toStatus") Integer toStatus
    );

    @Update("""
            UPDATE mall_order
            SET status = #{timeoutStatus},
                update_time = NOW()
            WHERE id = #{orderId}
              AND status = #{waitPayStatus}
              AND expire_time <= NOW()
            """)
    int timeoutClose(
            @Param("orderId") Long orderId,
            @Param("waitPayStatus") Integer waitPayStatus,
            @Param("timeoutStatus") Integer timeoutStatus
    );
}

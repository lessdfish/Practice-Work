package com.mall.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.inventory.domain.InventoryReservation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * ClassName:InventoryReservationMapper
 * Package:com.mall.inventory.mapper
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/4 - 16:14
 * @Version: v1.0
 *
 */
public interface InventoryReservationMapper extends BaseMapper<InventoryReservation> {
    @Update("""
            UPDATE inventory_reservation
            SET status = #{toStatus},
                update_time = NOW()
            WHERE order_id = #{orderId}
              AND status = #{fromStatus}
            """)
    int transitionStatus(
            @Param("orderId") Long orderId,
            @Param("fromStatus") Integer fromStatus,
            @Param("toStatus") Integer toStatus
    );
}

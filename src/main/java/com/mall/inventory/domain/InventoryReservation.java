package com.mall.inventory.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * ClassName:InventoryReservation
 * Package:com.mall.inventory.domain
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/4 - 16:11
 * @Version: v1.0
 *
 */
@Data
@TableName("inventory_reservation")
public class InventoryReservation {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long orderId;
    private Long productId;
    private Integer quantity;
    private Integer status;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

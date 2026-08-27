package com.mall.order.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ClassName:Order
 * Package:com.mall.order.domain
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/27 - 00:02
 * @Version: v1.0
 *
 */
@Data
@TableName("mall_order")
public class Order {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long productId;
    private Integer quantity;
    private BigDecimal amount;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

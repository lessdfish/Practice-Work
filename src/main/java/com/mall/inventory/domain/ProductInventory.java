package com.mall.inventory.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * ClassName:ProductInventory
 * Package:com.mall.inventory.domain
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 22:02
 * @Version: v1.0
 *
 */
@Data
@TableName("product_inventory")
public class ProductInventory {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long productId;
    private Integer availableStock;
    private Long version;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

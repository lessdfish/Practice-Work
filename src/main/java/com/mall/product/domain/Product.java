package com.mall.product.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ClassName:Product
 * Package:com.mall.product.domain
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/26 - 22:41
 * @Version: v1.0
 *
 */
@Data
@TableName("product")
public class Product {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

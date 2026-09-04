package com.mall.payment.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ClassName:Payment
 * Package:com.mall.payment.domain
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/4 - 15:08
 * @Version: v1.0
 *
 */
@Data
@TableName("mall_payment")
public class Payment {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String paymentNo;
    private Long orderId;
    private Long userId;
    private BigDecimal amount;
    private Integer status;
    private String thirdPartyTradeNo;
    private LocalDateTime paidTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

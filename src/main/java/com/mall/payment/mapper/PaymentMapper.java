package com.mall.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.payment.domain.Payment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

/**
 * ClassName:PaymentMapper
 * Package:com.mall.payment.mapper
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/4 - 15:16
 * @Version: v1.0
 *
 */
public interface PaymentMapper extends BaseMapper<Payment> {
    @Update("""
            UPDATE mall_payment
            SET status = #{successStatus},
                third_party_trade_no = #{tradeNo},
                paid_time = NOW(),
                update_time = now()
            WHERE payment_no = #{paymentNo}
              AND status = #{waitStatus}
              AND amount = #{amount}
            """)
    int markPaid(
            @Param("paymentNo") String paymentNo,
            @Param("tradeNo") String tradeNo,
            @Param("amount") BigDecimal amount,
            @Param("waitStatus") Integer waitStatus,
            @Param("successStatus") Integer successStatus
            );
}

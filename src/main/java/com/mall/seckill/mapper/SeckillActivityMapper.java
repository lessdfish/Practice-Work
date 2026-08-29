package com.mall.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.seckill.domain.SeckillActivity;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * ClassName:SeckillActivityMapper
 * Package:com.mall.seckill.mapper
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 23:02
 * @Version: v1.0
 *
 */

public interface SeckillActivityMapper extends BaseMapper<SeckillActivity> {
    @Update("""
            UPDATE seckill_activity
            SET available_stock =
                            available_stock - 1
            WHERE id = #{activityId}
            AND available_stock > 0
            AND status = 1
            """)
    int deductActivityStock(@PathVariable("activityId") Long activityId);
}

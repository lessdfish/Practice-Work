package com.mall.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.inventory.domain.ProductInventory;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * ClassName:ProductInventoryMapper
 * Package:com.mall.inventory.mapper
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 22:05
 * @Version: v1.0
 *
 */
public interface ProductInventoryMapper extends BaseMapper<ProductInventory> {
    @Update("""
            UPDATE product_inventory
            SET available_stock = available_stock - #{quantity},
                locked_stock = locked_stock + #{quantity}
            WHERE product_id = #{productId}
              AND available_stock >= #{quantity}
            """)
    int reserve(
            @Param("productId") Long productId,
            @Param("quantity") Integer quantity
    );

    @Update("""
            UPDATE product_inventory
            SET locked_stock = locked_stock - #{quantity},
                sold_stock = sold_stock + #{quantity}
            WHERE product_id = #{productId}
              AND locked_stock >= #{quantity}
            """)
    int confirm(
            @Param("productId") Long productId,
            @Param("quantity") Integer quantity
    );

    @Update("""
            UPDATE product_inventory
            SET locked_stock = locked_stock - #{quantity},
                available_stock = available_stock + #{quantity}
            WHERE product_id = #{productId}
              AND locked_stock >= #{quantity}
            """)
    int release(
            @Param("productId") Long productId,
            @Param("quantity") Integer quantity
    );
}

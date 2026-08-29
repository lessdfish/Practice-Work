package com.mall.inventory.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall.common.exception.BusinessException;
import com.mall.common.exception.ErrorCode;
import com.mall.inventory.domain.ProductInventory;
import com.mall.inventory.mapper.ProductInventoryMapper;
import org.springframework.stereotype.Service;

/**
 * ClassName:InventoryServiceImpl
 * Package:com.mall.inventory.service
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 22:10
 * @Version: v1.0
 *
 */
@Service
public class InventoryServiceImpl implements InventoryService{
    private final ProductInventoryMapper inventoryMapper;

    public InventoryServiceImpl(ProductInventoryMapper inventoryMapper) {
        this.inventoryMapper = inventoryMapper;
    }

    @Override
    public ProductInventory getByProductId(Long productId) {
        ProductInventory inventory = inventoryMapper.selectOne(new LambdaQueryWrapper<ProductInventory>()
                .eq(ProductInventory::getProductId,
                        productId).last("LIMIT 1"));
        if (inventory == null) {
            throw new BusinessException(ErrorCode.INVENTORY_NOT_FOUND);
        }
        return inventory;
    }

    @Override
    public void deductStock(Long productId, Integer quantity) {
        int affectedRows = inventoryMapper.deductStock(productId,quantity);
        if (affectedRows == 0){
            throw new BusinessException(ErrorCode.STOCK_NOT_ENOUGH);
        }
    }
}

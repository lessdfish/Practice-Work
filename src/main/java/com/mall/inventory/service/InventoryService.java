package com.mall.inventory.service;

import com.mall.inventory.domain.ProductInventory;

/**
 * ClassName:InventoryService
 * Package:com.mall.inventory.service
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 22:09
 * @Version: v1.0
 *
 */
public interface InventoryService {
    ProductInventory getByProductId(Long productId);

    void deductStock(Long productId,Integer quantity);
}

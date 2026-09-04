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

    void reserveStock(Long orderId,Long productId,Integer quantity);

    void confirmStock(Long orderId);

    void releaseStock(Long orderId);
}

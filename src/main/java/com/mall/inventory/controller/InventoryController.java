package com.mall.inventory.controller;

import com.mall.common.result.Result;
import com.mall.inventory.domain.ProductInventory;
import com.mall.inventory.service.InventoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName:InventoryController
 * Package:com.mall.inventory.controller
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 22:19
 * @Version: v1.0
 *
 */
@RestController
@RequestMapping("/api/admin/inventories")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/{productId}")
    public Result<ProductInventory> getInventory(@PathVariable Long productId){
        return Result.success(inventoryService.getByProductId(productId));
    }
}

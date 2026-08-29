package com.mall.admin.controller;

import com.mall.common.result.Result;
import com.mall.product.service.ProductService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName:AdminProductController
 * Package:com.mall.admin.controller
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 14:40
 * @Version: v1.0
 *
 */
@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/{id}/off-shelf")
    public Result<Void> offShelf(@PathVariable Long id){
        productService.updateProductStatus(id,0);
        return Result.success();
    }

    @PostMapping("/{id}/on-shelf")
    public Result<Void> onShelf(@PathVariable Long id){
        productService.updateProductStatus(id,1);
        return Result.success();
    }
}

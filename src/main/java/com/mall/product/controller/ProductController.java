package com.mall.product.controller;

import com.mall.common.result.Result;
import com.mall.product.domain.Product;
import com.mall.product.service.ProductService;
import lombok.val;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ClassName:ProductController
 * Package:com.mall.product.controller
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/26 - 22:51
 * @Version: v1.0
 *
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public Result<Product> getProduct(@PathVariable Long id){
        Product product = productService.getProductById(id);
        return Result.success(product);
    }

    @GetMapping
    public Result<List<Product>> listProducts(){
        return Result.success(productService.listProducts());
    }
}

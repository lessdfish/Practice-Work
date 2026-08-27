package com.mall.product.controller;

import com.mall.product.domain.Product;
import com.mall.product.service.ProductService;
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
    public Product getProduct(@PathVariable Long id){
        return productService.getProductById(id);
    }

    @GetMapping
    public List<Product> listProducts(){
        return productService.listProducts();
    }
}

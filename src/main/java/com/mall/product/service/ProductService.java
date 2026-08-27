package com.mall.product.service;

import com.mall.product.domain.Product;

import java.util.List;

/**
 * ClassName:ProductService
 * Package:com.mall.product.service
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/26 - 22:47
 * @Version: v1.0
 *
 */
public interface ProductService {
    Product getProductById(Long id);

    List<Product> listProducts();
}

package com.mall.product.service;

import com.mall.product.domain.Product;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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
    Map<Long,Product> getProductByIds(Collection<Long> productIds);

    Product getProductById(Long id);

    List<Product> listProducts();

    void updateProductStatus(Long productId,Integer status);
}

package com.mall.product.service;

import com.mall.common.aspect.OperationLog;
import com.mall.product.domain.Product;
import com.mall.product.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ClassName:ProductServiceImpl
 * Package:com.mall.product.service
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/26 - 22:48
 * @Version: v1.0
 *
 */
@Service
public class ProductServiceImpl implements ProductService{
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductMapper productMapper){
        this.productMapper = productMapper;
    }

    @Override
    @OperationLog("Finding specific")
    public Product getProductById(Long id){
        return productMapper.selectById(id);
    }

    @Override
    public List<Product> listProducts() {
        return productMapper.selectList(null);
    }
}

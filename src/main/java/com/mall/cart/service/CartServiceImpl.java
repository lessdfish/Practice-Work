package com.mall.cart.service;

import com.mall.cart.dto.AddCartItemRequest;
import com.mall.cart.dto.CartItemResponse;
import com.mall.common.exception.BusinessException;
import com.mall.common.exception.ErrorCode;
import com.mall.common.redis.RedisKeyConstants;
import com.mall.product.domain.Product;
import com.mall.product.service.ProductService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ClassName:CartServiceImpl
 * Package:com.mall.cart.service
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 18:22
 * @Version: v1.0
 *
 */
@Service
//TODO: N+1 PROBLEM
public class CartServiceImpl implements CartService{
    private static final Duration CART_TTL = Duration.ofDays(7);

    private final StringRedisTemplate stringRedisTemplate;

    private final ProductService productService;

    public CartServiceImpl(StringRedisTemplate stringRedisTemplate, ProductService productService) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.productService = productService;
    }

    @Override
    public List<CartItemResponse> listItems(Long userId) {
        String key = RedisKeyConstants.cart(userId);

        Map<Object,Object> entries = stringRedisTemplate.opsForHash().entries(key);
        if (entries.isEmpty()) {
            return List.of();
        }
        List<CartItemResponse> result = new ArrayList<>();

        for(Map.Entry<Object,Object> entry : entries.entrySet()){
            Long productId = Long.valueOf(entry.getKey().toString());
            Integer quantity = Integer.valueOf(entry.getValue().toString());

            Product product;
            try {
                product = productService.getProductById(productId);
            }catch (BusinessException e){
                continue;
            }
            CartItemResponse item = new CartItemResponse();
            item.setProductId(productId);
            item.setProductName(product.getName());
            item.setPrice(product.getPrice());
            item.setQuantity(quantity);

            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));

            item.setSubtotal(subtotal);

            result.add(item);
        }
        return result;
    }

    @Override
    public void addItem(Long userId, AddCartItemRequest request) {
        Product product = productService.getProductById(request.getProductId());
        if (product.getStatus() !=1) {
            throw new BusinessException(ErrorCode.PRODUCT_OFF_SHELF);
        }
        String key = RedisKeyConstants.cart(userId);
        String field = String.valueOf(request.getProductId());

        stringRedisTemplate.opsForHash().increment(key,field,request.getQuantity());

        stringRedisTemplate.expire(key,CART_TTL);
    }

    @Override
    public void updateQuantity(Long userId, Long productId, Integer quantity) {
        String key = RedisKeyConstants.cart(userId);
        String field = String.valueOf(productId);

        Boolean exists = stringRedisTemplate.opsForHash().hasKey(key,field);
        if (!Boolean.TRUE.equals(exists)) {
            throw new BusinessException(ErrorCode.CART_ITEM_NOT_FOUND);
        }
        stringRedisTemplate.opsForHash().put(key,field,String.valueOf(quantity));
        stringRedisTemplate.expire(key,CART_TTL);
    }

    @Override
    public void clear(Long userId) {
        stringRedisTemplate.delete(RedisKeyConstants.cart(userId));
    }

    @Override
    public void removeItem(Long userId, Long productId) {
        String key = RedisKeyConstants.cart(userId);
        stringRedisTemplate.opsForHash().delete(key,String.valueOf(productId));
    }
}

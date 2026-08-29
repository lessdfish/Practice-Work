package com.mall.ranking.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * ClassName:ProductRankingResponse
 * Package:com.mall.ranking.dto
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 21:10
 * @Version: v1.0
 *
 */
@Data
public class ProductRankingResponse {
    private Integer rank;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Long orderQuantity;

}

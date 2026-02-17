package com.deepakraj.cart.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CartResponse {

    private Long id;
    private String userId;
    private String productId;
    private Integer quantity;
    private BigDecimal priceAtAddTime;
}

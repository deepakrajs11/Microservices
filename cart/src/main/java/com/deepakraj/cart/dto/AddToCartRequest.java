package com.deepakraj.cart.dto;

import lombok.Data;

@Data
public class AddToCartRequest {
    private Long userId;
    private String productId;
    private Integer quantity;
}

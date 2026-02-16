package com.deepakraj.order.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {

    private String productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
}

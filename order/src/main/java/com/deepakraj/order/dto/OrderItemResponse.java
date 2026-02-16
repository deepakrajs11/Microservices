package com.deepakraj.order.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemResponse {

    private String productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
}

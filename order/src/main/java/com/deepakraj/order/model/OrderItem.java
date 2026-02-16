package com.deepakraj.order.model;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    private String productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
}

package com.deepakraj.order.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

    private String userId;
    private List<OrderItemRequest> items;
}

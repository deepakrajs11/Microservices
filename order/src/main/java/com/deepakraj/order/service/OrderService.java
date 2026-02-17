package com.deepakraj.order.service;

import com.deepakraj.order.dto.CreateOrderRequest;
import com.deepakraj.order.dto.OrderResponse;
import java.util.List;

public interface OrderService {

    OrderResponse createOrder(CreateOrderRequest request);

    OrderResponse getOrderById(String id);

    List<OrderResponse> getOrdersByUserId(Long userId);

    OrderResponse updateOrderStatus(String id, String status);

    void deleteOrder(String id);
}

